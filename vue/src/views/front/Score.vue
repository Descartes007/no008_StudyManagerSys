<template>
  <div class="main-content">
    <div style="width: 70%; margin: 30px auto">
      <div style="margin-bottom: 20px">
        <el-input placeholder="请输入课程名称" style="width: 200px" size="mini" v-model="name"></el-input>
        <el-button type="info" plain style="margin-left: 10px" size="mini" @click="load(1)">查询</el-button>
        <el-button type="warning" plain style="margin-left: 10px" size="mini" @click="reset">重置</el-button>
      </div>
      <div class="table">
        <el-table :data="tableData" stripe>
          <el-table-column prop="id" label="序号" width="80" align="center" sortable></el-table-column>
          <el-table-column prop="img" label="课程封面" show-overflow-tooltip width="100">
            <template v-slot="scope">
              <div style="display: flex; align-items: center">
                <el-image style="width: 60px; height: 40px; border-radius: 5px; border: 1px solid #cccccc" v-if="scope.row.img"
                          :src="scope.row.img" :preview-src-list="[scope.row.img]"></el-image>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="name" label="课程名称" show-overflow-tooltip width="400">
            <template v-slot="scope">
              <a :href="'/front/scoreDetail?id=' + scope.row.id">{{ scope.row.name }}</a>
            </template>
          </el-table-column>
          <el-table-column prop="type" label="课程类型">
            <template v-slot="scope">
              <span v-if="scope.row.type === 'VIDEO'" style="color: #b67259">视频课</span>
              <span v-else style="color: #448231">图文课</span>
            </template>
          </el-table-column>
          <el-table-column prop="price" label="所属积分">
            <template v-slot="scope">
              <span style="color: #12b127; font-size: 15px" v-if="scope.row.price > 0">{{ scope.row.price }} 积分</span>
              <span v-else style="color: green">公开课</span>
            </template>
          </el-table-column>
          <el-table-column prop="time" label="发布时间"></el-table-column>
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
  </div>
</template>
<script>

export default {

  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      tableData: [],  // 所有的数据
      pageNum: 1,   // 当前的页码
      pageSize: 6,  // 每页显示的个数
      total: 0,
      name: null,
    }
  },
  mounted() {
    this.load(1)
  },
  // methods：本页面所有的点击事件或者其他函数定义区
  methods: {
    load(pageNum) {  // 分页查询
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/score/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
        }
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    reset() {
      this.name = null
      this.load(1)
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
  }
}
</script>
