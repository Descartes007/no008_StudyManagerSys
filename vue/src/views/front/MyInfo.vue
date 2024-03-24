<template>
  <div class="main-content">
    <div style="width: 80%; margin: 30px auto">
      <div style="margin: 20px 0">
        <el-input placeholder="请输入资料名称" style="width: 200px" size="mini" v-model="name"></el-input>
        <el-button type="info" plain style="margin-left: 10px" size="mini" @click="load(1)">查询</el-button>
        <el-button type="warning" plain style="margin-left: 10px" size="mini" @click="reset">重置</el-button>
        <el-button type="primary" plain size="mini" @click="handleAdd">发布资料</el-button>
        <el-button type="danger" plain size="mini" @click="delBatch">批量删除</el-button>
      </div>

      <div class="table">
        <el-table :data="tableData" stripe  @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55" align="center"></el-table-column>
          <el-table-column prop="id" label="序号" width="80" align="center" sortable></el-table-column>
          <el-table-column prop="img" label="资料封面" width="100">
            <template v-slot="scope">
              <div style="display: flex; align-items: center">
                <el-image style="width: 60px; height: 40px; border-radius: 10px" v-if="scope.row.img"
                          :src="scope.row.img" :preview-src-list="[scope.row.img]"></el-image>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="name" label="资料名称" width="250" show-overflow-tooltip></el-table-column>
          <el-table-column prop="userName" label="上传用户"></el-table-column>
          <el-table-column prop="time" label="上传时间"></el-table-column>
          <el-table-column prop="recommend" label="是否推荐"></el-table-column>
          <el-table-column prop="score" label="所需积分"></el-table-column>
          <el-table-column prop="status" label="审核状态"></el-table-column>
          <el-table-column prop="descr" label="审核说明"></el-table-column>

          <el-table-column label="操作" width="180" align="center">
            <template v-slot="scope">
              <el-button plain type="primary" @click="handleEdit(scope.row)" size="mini">编辑</el-button>
              <el-button plain type="danger" size="mini" @click=del(scope.row.id)>删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <div class="pagination" style="margin-top: 20px">
          <el-pagination
              background
              @current-change="handleCurrentChange"
              :current-page="pageNum"
              :page-sizes="[5, 10, 20]"
              :page-size="pageSize"
              layout="total, prev, pager, next"
              :total="total">
          </el-pagination>
        </div>
      </div>
    </div>

    <el-dialog title="课程信息" :visible.sync="fromVisible" width="55%" :close-on-click-modal="false" destroy-on-close>
      <el-form label-width="100px" style="padding-right: 50px" :model="form" :rules="rules" ref="formRef">
        <el-form-item label="资料封面">
          <el-upload
              class="avatar-uploader"
              :action="$baseUrl + '/files/upload'"
              :headers="{ token: user.token }"
              list-type="picture"
              :on-success="handleImgSuccess"
          >
            <el-button type="primary">上传图片</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item prop="name" label="资料名称">
          <el-input v-model="form.name" autocomplete="off" placeholder="请输入资料名称"></el-input>
        </el-form-item>
        <el-form-item prop="score" label="所需积分">
          <el-input v-model="form.score" autocomplete="off" placeholder="请输入积分（0表示无需积分）"></el-input>
        </el-form-item>
        <el-form-item prop="file" label="资料链接">
          <el-input v-model="form.file" autocomplete="off" placeholder="请输入资料链接"></el-input>
        </el-form-item>
        <el-form-item prop="content" label="资料介绍">
          <div id="editor"></div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import E from 'wangeditor'
export default {
  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      editor: null,
      tableData: [],  // 所有的数据
      pageNum: 1,   // 当前的页码
      pageSize: 5,  // 每页显示的个数
      total: 0,
      name: null,
      recommend: null,
      fromVisible: false,
      form: {},
      rules: {
        name: [
          {required: true, message: '请输入资料名称', trigger: 'blur'},
        ]
      },
      ids: []
    }
  },
  mounted() {
    this.load(1)
  },
  // methods：本页面所有的点击事件或者其他函数定义区
  methods: {
    initWangEditor(content) {
      this.$nextTick(() => {
        this.editor = new E('#editor')
        this.editor.config.placeholder = '请输入内容'
        this.editor.config.uploadFileName = 'file'
        this.editor.config.uploadImgServer = 'http://localhost:9090/files/wang/upload'
        this.editor.create()
        setTimeout(() => {
          this.editor.txt.html(content)
        })
      })
    },
    handleAdd() {   // 新增数据
      this.form = {
        userId: this.user.id,
      }
      this.fromVisible = true   // 打开弹窗
      this.initWangEditor('')
    },
    handleEdit(row) {   // 编辑数据
      this.form = JSON.parse(JSON.stringify(row))  // 给form对象赋值  注意要深拷贝数据
      this.fromVisible = true   // 打开弹窗
      this.initWangEditor(this.form.content || '')
    },
    save() {   // 保存按钮触发的逻辑  它会触发新增或者更新
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.form.status = '待审核'
          this.form.descr = ''
          this.form.content = this.editor.txt.html()
          this.$request({
            url: this.form.id ? '/information/update' : '/information/add',
            method: this.form.id ? 'PUT' : 'POST',
            data: this.form
          }).then(res => {
            if (res.code === '200') {  // 表示成功保存
              this.$message.success('保存成功')
              this.load(1)
              this.fromVisible = false
            } else {
              this.$message.error(res.msg)  // 弹出错误的信息
            }
          })
        }
      })
    },
    del(id) {   // 单个删除
      this.$confirm('您确定删除吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/information/delete/' + id).then(res => {
          if (res.code === '200') {   // 表示操作成功
            this.$message.success('操作成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(() => {
      })
    },
    delBatch() {   // 批量删除
      if (!this.ids.length) {
        this.$message.warning('请选择数据')
        return
      }
      this.$confirm('您确定批量删除这些数据吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/information/delete/batch', {data: this.ids}).then(res => {
          if (res.code === '200') {   // 表示操作成功
            this.$message.success('操作成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(() => {
      })
    },
    load(pageNum) {  // 分页查询
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/information/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
          userId: this.user.id
        }
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
    handleSelectionChange(rows) {   // 当前选中的所有的行数据
      this.ids = rows.map(v => v.id)   //  [1,2]
    },
    handleImgSuccess(res) {
      this.form.img = res.data
    },
    reset() {
      this.name = null
      this.load(1)
    },
  }
}
</script>
