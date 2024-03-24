<template>
  <div class="main-content">
    <div style="display: flex">
      <div style="flex: 12%; height: 350px; background-color: #0c0c0c"></div>
      <div style="flex: 76%">
        <el-carousel height="350px">
          <el-carousel-item v-for="item in carouselData">
            <img :src="item" alt="" style="width: 100%; height: 350px">
          </el-carousel-item>
        </el-carousel>
      </div>
      <div style="flex: 12%; height: 350px; background-color: #0c0c0c"></div>
    </div>
    <div style="width: 76%; margin: 50px auto; min-height: 1000px">
      <div style="display: flex">
        <div style="flex: 1">
          <span style="font-size: 24px; color: #333333; margin-right: 50px">在线课程</span>
          <el-button type="primary" @click="initValue('VIDEO')">视频课程</el-button>
          <el-button type="success" @click="initValue('SCORE')">积分专区</el-button>
          <el-button type="warning" @click="initValue('TEXT')">图文课程</el-button>
        </div>
        <div style="width: 300px">
          <el-button type="info" @click="signin">签到</el-button>
          <span style="margin-left: 30px; color: #12b127; font-weight: 550">上次签到：</span>
          <span style="color: #666666">{{ signInData?.time }}</span>
        </div>
      </div>
      <div style="display: flex; margin-top: 20px; height: 300px">
        <div style="flex: 1; margin-right: 10px; width: 0">
          <img :src="recommend.img" alt="" style="width: 100%; height: 270px; border-radius: 5px; cursor: pointer" @click="navTo(recommend.id)">
          <div style="font-size: 15px; margin-top: 5px" class="overflowShow">{{ recommend.name }}</div>
        </div>
        <div style="flex: 2; margin-left: 10px">
          <el-row :gutter="20">
            <el-col :span="6" style="margin-bottom: 35px" v-for="item in rightData">
              <img :src="item.img" alt="" style="width: 100%; height: 100px; border-radius: 5px; border: 1px solid #cccccc; cursor: pointer" @click="navTo(item.id)">
              <div style="color: #333333; margin-top: 10px" class="overflowShow">{{ item.name }}</div>
            </el-col>
          </el-row>
        </div>
      </div>

      <div style="margin-top: 50px">
        <span style="font-size: 24px; color: #333333; margin-right: 50px">在线资源</span>
      </div>
      <div style="display: flex; margin-top: 20px; height: 300px">
        <div style="flex: 2; margin-right: 10px">
          <el-row :gutter="20">
            <el-col :span="6" style="margin-bottom: 35px" v-for="item in leftData">
              <img :src="item.img" alt="" style="width: 100%; height: 100px; border-radius: 5px; border: 1px solid #cccccc; cursor: pointer" @click="navToInformation(item.id)">
              <div style="color: #333333; margin-top: 10px" class="overflowShow">{{ item.name }}</div>
            </el-col>
          </el-row>
        </div>
        <div style="flex: 1; margin-left: 10px; width: 0">
          <img :src="fileRecommend?.img" alt="" style="width: 100%; height: 270px; border-radius: 5px; cursor: pointer" @click="navToInformation(fileRecommend.id)">
          <div style="font-size: 15px; margin-top: 5px" class="overflowShow">{{ fileRecommend?.name }}</div>
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
      carouselData: [
          require('@/assets/imgs/lun-1.jpg'),
          require('@/assets/imgs/lun-2.jpg'),
          require('@/assets/imgs/lun-3.jpg'),
          require('@/assets/imgs/lun-4.jpg'),
      ],
      type: 'VIDEO',
      recommend: {},
      rightData: [],
      fileRecommend: {},
      leftData: [],
      signInData: {}
    }
  },
  mounted() {
    this.getData()
    this.getInformation()
    this.getSign()
  },
  // methods：本页面所有的点击事件或者其他函数定义区
  methods: {
    getSign() {
      this.$request.get('/signin/selectByUserId?id=' + this.user.id).then(res => {
        if (res.code === '200') {
          // 渲染数据到页面
          this.signInData = res.data
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    signin() {
      let data = {
        userId: this.user.id
      }
      this.$request.post('/signin/add', data).then(res => {
        if (res.code === '200') {
          this.$message.success('签到成功，恭喜你获得10个积分')
          this.getSign()
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    navTo(id) {
      if (this.type === 'SCORE') {
        // 我们往积分课程详情页跳
        location.href = '/front/scoreDetail?id=' + id
      } else {
        // 往课程详情页跳
        location.href = '/front/courseDetail?id=' + id
      }
    },
    navToInformation(id) {
      location.href = '/front/informationDetail?id=' + id
    },
    initValue(type) {
      this.type = type
      this.getData()
    },
    getData() {
      // 积分专区这边的数据
      if ('SCORE' === this.type) {
        // 1. 获取推荐的那个积分课程
        this.getRecommend('/score/getRecommend')
        // 2. 获取推荐之外的最新的8个课程
        this.getRightData('/score/getTop8')
      } else {
        // 1. 获取推荐的那个视频课程
        this.getRecommend('/course/getRecommend?type=' + this.type)
        // 2. 获取推荐之外的最新的8个视频课程
        this.getRightData('/course/selectTop8?type=' + this.type)
      }
    },

    getRecommend(url) {
      this.$request.get(url).then(res => {
        if (res.code === '200') {
          this.recommend = res.data
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    getRightData(url) {
      this.$request.get(url).then(res => {
        if (res.code === '200') {
          this.rightData = res.data
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    getInformation() {
      // 1. 获取推荐的那个资料
      this.$request.get('/information/getRecommend').then(res => {
        if (res.code === '200') {
          this.fileRecommend = res.data
        } else {
          this.$message.error(res.msg)
        }
      })
      // 2. 获取非推荐的8个资料
      this.$request.get('/information/selectTop8').then(res => {
        if (res.code === '200') {
          this.leftData = res.data
        } else {
          this.$message.error(res.msg)
        }
      })
    },
  }
}
</script>
