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
                  @blur="handleBlur"
                  @keyup.enter="handleConfirm"
                  @keyup.esc="handleCancel"
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
        <a-icon class="card-button-item" type="check" @mousedown.prevent="handleConfirm"/>
        <a-icon class="card-button-item" type="close" @mousedown.prevent="handleCancel"/>
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
      title: this.info.conversationTitle,
      isSubmitting: false, // 防止重复提交
    }
  },
  methods: {
    handleDelete() {
      // 防止重复点击
      if (this.isSubmitting) {
        return;
      }
      this.isSubmitting = true;
      
      // 触发删除事件
      this.$emit('delete', this.info);
      
      // 延迟重置状态，防止快速重复点击
      setTimeout(() => {
        this.isSubmitting = false;
      }, 1000);
    },
    handleConfirm() {
      // 防止重复提交
      if (this.isSubmitting) {
        return;
      }
      
      // 取消编辑状态
      this.isEdit = false;
      
      // 检查标题是否有变化
      if (this.info.conversationTitle === this.title) {
        return;
      }
      
      // 设置提交中状态
      this.isSubmitting = true;
      
      // 触发修改标题事件
      this.$emit('changeTitle', this.info, this.title);
      
      // 延迟重置状态
      setTimeout(() => {
        this.isSubmitting = false;
      }, 1000);
    },
    handleCancel() {
      // 取消编辑，恢复原标题
      this.isEdit = false;
      this.title = this.info.conversationTitle;
    },
    handleBlur() {
      // blur时不做任何操作，等待用户点击check或close按钮
      // 这样可以避免blur和mousedown事件冲突
    },
    handleClickEdit() {
      // 编辑时不允许删除或其他操作
      if (this.isSubmitting) {
        return;
      }
      
      this.isEdit = true;
      this.$nextTick(() => {
        const inputs = this.$el.querySelectorAll('.el-input__inner');
        if (inputs && inputs.length > 0) {
          inputs[0].focus();
        }
      });
    }
  },
  computed: {},
  mounted() {
  },
  watch: {
    // 监听info变化，同步更新title
    'info.conversationTitle': {
      handler(newVal) {
        if (!this.isEdit) {
          this.title = newVal;
        }
      },
      immediate: true
    }
  }
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