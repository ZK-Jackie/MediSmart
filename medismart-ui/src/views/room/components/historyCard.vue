<template>
  <a-card hoverable class="card-detail" size="small"
          :style="isSelected? 'border: 3px solid rgb(153,204,204)' : ''"
          @click="$emit('click', info)"
  >
    <el-col :span="18">
      <!-- 记录标题 -->
      <div class="card-title-input"
           v-if="isEdit"
      >
        <el-input type="text"
                  ref="titleInput"
                  placeholder="输入记录标题"
                  v-model="title"
                  maxlength="15"
                  show-word-limit
                  size="mini"
                  @blur="handleSave(false)"
        >
        </el-input>
      </div>
      <div class="card-title" @dblclick="handleClickEdit()" v-else>
        {{ title }}
      </div>
    </el-col>
    <!-- 记录按键 -->
    <el-col :span="6">
      <div class="card-buttons" v-if="isEdit">
        <a-icon class="card-button-item" type="check" @mousedown="handleSave(true)"/>
        <a-icon class="card-button-item" type="close" @mousedown="handleSave(false)"/>
      </div>
      <div class="card-buttons" v-else>
        <a-icon class="card-button-item" type="edit" @click="isEdit = true"/>
        <a-popconfirm title="该操作将永远删除本次对话，是否确认删除？"
                      ok-text="删除"
                      cancel-text="取消"
                      @confirm="$emit('delete', info)"
        >
          <a-icon class="card-button-item" type="delete"/>
        </a-popconfirm>
      </div>
    </el-col>
  </a-card>
</template>


<script>
export default {
  name: "HistoryCard",
  props: {
    isSelected: {
      type: Boolean,
      default: false
    },
    info: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  data() {
    return {
      isEdit: false,
      isSave: false,
      title: this.info.conversationTitle
    }
  },
  methods: {
    handleDelete() {

    },
    handleSave(blurSignal) {
      if(blurSignal)
        this.isSave = blurSignal;
      //等等按键的 isSave 信号
      setTimeout(() => {
        if(!this.isSave){
          this.isEdit = false;
          this.title = this.info.conversationTitle;
          return;
        }else{
          this.isEdit = false;
          if(this.info.conversationTitle === this.title) return;
          this.$emit('changeTitle', this.info, this.title);
        }
      }, 100);
    },
    handleClickEdit() {
      this.isEdit = true;
      this.$nextTick(() => {
        document.getElementsByClassName("el-input__inner")[0].focus();
      });
    }
  },
  computed: {},
  mounted() {
  },
}

</script>

<style scoped>
a-card {
  box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2);
  transition: 0.3s;
}

.card-detail {
  width: 100%;
  border-radius: 1px;
}

.card-title-input {
  width: auto;
}

.card-button-item {
  margin-left: 0.5rem;
}
</style>