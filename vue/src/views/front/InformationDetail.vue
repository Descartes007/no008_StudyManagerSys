<template>
  <div class="main-content">
    <div style="width: 70%; margin: 30px auto; min-height: 1000px">
      <div style="text-align: center">
        <span style="font-size: 20px; font-weight: 550; color: #333333; margin-left: 20px">{{ informationData.name }}</span>
      </div>
      <div style="text-align: center; margin-top: 15px">
        <span style="color: red" v-if="informationData.score > 0">下载该资源需要 {{ informationData.score }} 积分</span>
        <span style="color: #12b127" v-else>免费白嫖</span>
        <span style="color: #666666; margin-left: 50px">发布时间：{{ informationData.time }}</span>
      </div>
      <!--   资料保密区域   -->
      <div>
        <div style="font-size: 18px; margin: 10px 0">资料链接</div>
        <div v-if="informationData.score === 0 || flag">
          <div style="margin-top: 10px">资料链接：<a :href="informationData.file" target="_blank">{{ informationData.file }}</a></div>
        </div>
        <div v-else>
          <span style="color: red; margin-right: 20px">该资料需要积分，兑换后可解锁</span>
          <el-button type="warning" size="mini" @click="exchange">兑换资料</el-button>
        </div>
      </div>
      <!--   资料介绍区域   -->
      <div style="margin-top: 20px">
        <div style="font-size: 18px">资料介绍</div>
        <div style="margin-top: 10px" v-html="informationData.content" class="w-e-text w-e-text-container"></div>
      </div>
    </div>
  </div>
</template>
<script>
import E from 'wangeditor'
export default {

  data() {
    let informationId = this.$route.query.id
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      informationId: informationId,
      informationData: {},
      flag: false
    }
  },
  mounted() {
    this.loadInformation()
    this.check()
  },
  // methods：本页面所有的点击事件或者其他函数定义区
  methods: {
    check() {
      this.$request.get('/fileorder/selectAll', {
        params: {
          userId: this.user.id,
          fileId: this.informationId
        }
      }).then(res => {
        if (res.code === '200') {
          if (res.data?.length > 0) {
            this.flag = true
          }
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    exchange() {
      let data = {
        fileId: this.informationId,
        userId: this.user.id,
        score: this.informationData.score
      }
      this.$request.post('/fileorder/add', data).then(res => {
        if (res.code === '200') {
          this.$message.success('兑换成功')
          this.loadInformation()
          this.check()
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    loadInformation() {
      this.$request.get('/information/selectById/' + this.informationId).then(res => {
        if (res.code === '200') {
          this.informationData = res.data
        } else {
          this.$message.error(res.msg)
        }
      })
    }
  }
}
</script>
