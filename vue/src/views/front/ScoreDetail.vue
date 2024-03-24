<template>
  <div class="main-content">
    <div style="width: 70%; margin: 30px auto; min-height: 1000px">
      <div style="text-align: center">
        <el-button type="success">{{ courseData.type === 'VIDEO'? '视频课' : '图文课' }}</el-button>
        <span style="font-size: 20px; font-weight: 550; color: #333333; margin-left: 20px">{{ courseData.name }}</span>
      </div>
      <div style="text-align: center; margin-top: 15px">
        <span style="color: #12b127" v-if="courseData.price > 0">学习该课程需要 {{ courseData.price }} 积分</span>
        <span style="color: red" v-else>公开课</span>
        <span style="color: #666666; margin-left: 50px">发布时间：{{ courseData.time }}</span>
      </div>
      <!--   课程保密区域   -->
      <div>
        <div style="font-size: 18px; margin: 10px 0">课程资料</div>
        <div v-if="courseData.price === 0 || flag">
          <video :src="courseData.video" v-if="courseData.type === 'VIDEO'" controls style="width: 65%; height: 400px"></video>
          <div style="margin-top: 10px">资料链接：<a :href="courseData.file" target="_blank">{{ courseData.file }}</a></div>
        </div>
        <div v-else>
          <span style="color: red; margin-right: 20px">该课程需要积分，兑换后可解锁</span>
          <el-button type="warning" size="mini" @click="exchange">兑换课程</el-button>
        </div>
      </div>
      <!--   课程介绍区域   -->
      <div style="margin-top: 20px">
        <div style="font-size: 18px">课程介绍</div>
        <div style="margin-top: 10px" v-html="courseData.content" class="w-e-text w-e-text-container"></div>
      </div>
    </div>
  </div>
</template>
<script>
import E from 'wangeditor'
export default {

  data() {
    let scoreId = this.$route.query.id
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      scoreId: scoreId,
      courseData: {},
      flag: false
    }
  },
  mounted() {
    this.loadCourse()
    this.checkOrder()
  },
  // methods：本页面所有的点击事件或者其他函数定义区
  methods: {
    checkOrder() {
      this.$request.get('/scoreorder/selectAll', {
        params: {
          userId: this.user.id,
          scoreId: this.scoreId
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
        scoreId: this.scoreId,
        score: this.courseData.price,
        userId: this.user.id
      }
      this.$request.post('/scoreorder/add', data).then(res => {
        if (res.code === '200') {
          this.$message.success('兑换成功')
          this.loadCourse()
          this.checkOrder()
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    loadCourse() {
      this.$request.get('/score/selectById/' + this.scoreId).then(res => {
        if (res.code === '200') {
          this.courseData = res.data
        } else {
          this.$message.error(res.msg)
        }
      })
    }
  }
}
</script>
