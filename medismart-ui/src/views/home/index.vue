<template>
  <div>
    <el-carousel :height="carouselHeight">
      <el-carousel-item v-for="(item, index) in carouselItems" :key="index">
        <img :alt="item.alt"
             class="pics"
             loading="lazy"
             :src="item.src"/>
        <div class="overlay">
          <div class="overlay-content">
            <h2 style="color: white">{{ item.title }}</h2>
            <p>{{ item.description }}</p>
            <el-button round @click="$router.push(item.buttonDes)">{{item.buttonName}}</el-button>
          </div>
        </div>
      </el-carousel-item>
    </el-carousel>

    <div class="intro">
      <section v-for="(item, index) in items" :key="index" :class="['section', index % 2 === 0 ? 'even' : 'odd']">
        <div class="content">
          <h2>{{ item.title }}</h2>
          <p>{{ item.description }}</p>
          <el-button round disabled>了解更多</el-button>
        </div>
        <div class="image-container">
          <img :src="item.image" alt="image" class="image" loading="lazy">
        </div>
      </section>
    </div>
  </div>
</template>

<script>
let pics = [];
pics.push(require('@/assets/section1.png'));
pics.push(require('@/assets/section2.png'));
pics.push(require('@/assets/section3.png'));
pics.push(require('@/assets/section4.png'));

let carouselPics = [];
carouselPics.push(require('@/assets/scroll-pic1.png'));
carouselPics.push(require('@/assets/scroll-pic2.jpg'));

export default {
  components: {},
  data() {
    return {
      carouselHeight: '500px',
      carouselItems: [
        {
          src: carouselPics[0],
          alt: 'carousel1',
          title: '多智能体应用问答',
          description: '',
          buttonDes: '/chat',
          buttonName: '进入问答'
        },
        {
          src: carouselPics[1],
          alt: 'carousel2',
          title: '结合知识图谱数据',
          description: '',
          buttonDes: '/graph',
          buttonName: '进入知识图谱'
        },
        // 可以添加更多的轮播项
      ],
      // 其他数据
      items: [
        {
          title: '大数据库，知识更加丰富',
          description: '我们精心打造的大数据库，汇集了众多领域的真实案例数据。无论您需要深入的市场调研、学术研究、趋势分析，还是个性化的数据查询，我们都能为您提供丰富、精准、多样化的信息资源，满足您的各种需求.',
          image: pics[0]
        },
        {
          title: '有理有据，支撑每次对话',
          description: '我们致力于深入理解用户需求，运用详实的医疗数据，为您提供全面且专业的回答。无论是疾病诊断、治疗方案还是健康咨询，我们的服务都将基于充分的证据和精准的数据分析，确保您获得最可靠的信息支持。让每一次对话都充满信心和信赖。',
          image: pics[1]
        },
        {
          title: '专家团队，保证回答“安全”',
          description: '我们可以让专业的医疗人士团队添加和校验知识图谱中的数据，确保信息的准确性和可靠性。通过这种专业入驻机制，我们不仅提高了知识图谱数据的可信度，还为用户提供了一个由专家支撑的、值得信赖的数据环境。您的每一次查询，都有我们的专业团队为您保驾护航。',
          image: pics[2]
        },
        {
          title: '多种选择，适配多种模型',
          description: '我们的平台具备灵活的对接能力，能够与全球各地的LLM（大型语言模型）无缝连接。无论您使用的是哪种模型，只要提供相应的API接口，我们都能快速适配，为您提供高效、便捷的服务。',
          image: pics[3]
        },
      ]
    }
  },
  mounted() {
    this.updateCarouselHeight();
    window.addEventListener('resize', this.updateCarouselHeight);
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.updateCarouselHeight);
  },
  methods: {
    updateCarouselHeight() {
      if (window.innerWidth <= 768) {
        this.carouselHeight = '300px';
      } else {
        this.carouselHeight = '500px';
      }
    }
  }
}
</script>

<style lang="scss">

.scroll-pics {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh; // 设置容器高度为视口的100%
  background-color: #fff; // 设置背景颜色为白色
}

.pics {
  position: absolute;
  height: 100%; // 限制图片的最大高度为100%
  right: 5%;
}

.overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background-color: rgba(0, 0, 0, 0.5); // 半透明的黑色背景
  color: white; // 文本颜色为白色
}

.overlay-content {
  position: absolute;
  left: 10%;
  
  h2 {
    font-size: 2rem;
    margin-bottom: 1rem;
  }
  
  p {
    font-size: 1rem;
    margin-bottom: 1rem;
  }
}

.intro {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-left: 5%;
  margin-right: 5%;
}

.section {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 5%;
  margin-bottom: 5%;
}

.even {
  flex-direction: row;
}

.even .content {
  width: 30%;
  margin-left: 10%; // 添加这一行，使文本更靠近图片
}


.odd {
  flex-direction: row-reverse;
}

.odd .content {
  width: 30%;
  margin-right: 10%; // 添加这一行，使文本更靠近图片
}

.content {
  width: 30%;
  
  h2 {
    font-size: 1.8rem;
    margin-bottom: 1rem;
  }
  
  p {
    font-size: 1rem;
    line-height: 1.6;
    margin-bottom: 1.5rem;
  }
}

.image-container {
  width: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  color: #2c3e50;

}

.image {
  border-radius: 10px;
  width: 600px; // 指定的框架宽度
  height: 400px; // 指定的框架高度
  object-fit: contain; // 等比例压缩图片，同时保证图片完整显示
  border: 1px solid gray; // 添加这一行，设置边框颜色为灰色
  box-shadow: 5px 5px 3px 0px rgba(0, 0, 0, 0.5); // 添加这一行，设置右下角的阴影
}

// 移动端适配
@media screen and (max-width: 768px) {
  .pics {
    right: 0;
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
  
  .overlay-content {
    left: 5%;
    right: 5%;
    text-align: center;
    
    h2 {
      font-size: 1.5rem;
      margin-bottom: 0.5rem;
    }
    
    p {
      font-size: 0.9rem;
      margin-bottom: 0.8rem;
    }
  }
  
  .intro {
    margin-left: 2%;
    margin-right: 2%;
  }
  
  .section {
    flex-direction: column !important;
    margin-top: 8%;
    margin-bottom: 8%;
  }
  
  .even .content,
  .odd .content {
    width: 100%;
    margin-left: 0;
    margin-right: 0;
    padding: 0 5%;
    text-align: center;
    order: 2;
  }
  
  .content {
    width: 100%;
    
    h2 {
      font-size: 1.5rem;
      margin-bottom: 0.8rem;
    }
    
    p {
      font-size: 0.9rem;
      line-height: 1.5;
      margin-bottom: 1rem;
    }
  }
  
  .image-container {
    width: 100%;
    order: 1;
    margin-bottom: 1rem;
  }
  
  .image {
    width: 90%;
    max-width: 400px;
    height: auto;
    min-height: 200px;
  }
}

// 小尺寸手机适配
@media screen and (max-width: 480px) {
  .overlay-content {
    h2 {
      font-size: 1.2rem;
    }
    
    p {
      font-size: 0.8rem;
    }
    
    .el-button {
      font-size: 0.85rem;
      padding: 8px 15px;
    }
  }
  
  .content {
    h2 {
      font-size: 1.3rem;
    }
    
    p {
      font-size: 0.85rem;
    }
    
    .el-button {
      font-size: 0.85rem;
      padding: 8px 15px;
    }
  }
  
  .image {
    max-width: 100%;
    min-height: 150px;
  }
}

</style>