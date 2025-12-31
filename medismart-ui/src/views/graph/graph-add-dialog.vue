<template>
  <div>
    <el-dialog
        title="添加医疗知识节点"
        :visible.sync="value"
        :before-close="handleCancel">
      <el-steps class="add-dialog-header" :active="nowActiveStep" finish-status="success" simple>
        <el-step title="步骤 1"></el-step>
        <el-step title="步骤 2"></el-step>
      </el-steps>
      <div class="add-dialog-content">
        <el-form label-position="top" label-width="80px" v-if="nowActiveStep === 0">
          <el-form-item label="节点类型">
            <el-select v-model="nodeInfo.category" placeholder="选择结点类型">
              <el-option label="检查项目" value="Check"></el-option>
              <el-option label="检查科室" value="Department"></el-option>
              <el-option label="疾病" value="Disease"></el-option>
              <el-option label="药物" value="Drug"></el-option>
              <el-option label="食物" value="Food"></el-option>
              <el-option label="药厂" value="Producer"></el-option>
              <el-option label="症状" value="Symptom"></el-option>
            </el-select>
          </el-form-item>
        </el-form>

        <el-form v-if="nowActiveStep === 1">
          <el-form-item label="uid">
            <el-input v-model="nodeInfo.uid" disabled></el-input>
          </el-form-item>
          <el-form-item label="疾病名称">
            <el-input v-model="nodeInfo.name" placeholder="请输入节点名称"></el-input>
          </el-form-item>
          <el-form-item label="疾病原因">
            <el-input v-model="nodeInfo.cause" placeholder="请输入疾病原因"></el-input>
          </el-form-item>
          <el-form-item label="治疗科室">
            <el-select v-model="nodeInfo.cureDepartment" multiple placeholder="请选择治疗科室">
              <el-option label="内科" value="内科"></el-option>
              <el-option label="外科" value="外科"></el-option>
              <el-option label="妇产科" value="妇产科"></el-option>
              <el-option label="儿科" value="儿科"></el-option>
              <el-option label="五官科" value="五官科"></el-option>
              <el-option label="皮肤科" value="皮肤科"></el-option>
              <el-option label="肿瘤科" value="肿瘤科"></el-option>
              <el-option label="骨科" value="骨科"></el-option>
              <el-option label="心脏科" value="心脏科"></el-option>
              <el-option label="神经科" value="神经科"></el-option>
              <el-option label="肝胆科" value="肝胆科"></el-option>
              <el-option label="肾病科" value="肾病科"></el-option>
              <el-option label="肛肠科" value="肛肠科"></el-option>
              <el-option label="泌尿科" value="泌尿科"></el-option>
              <el-option label="风湿免疫科" value="风湿免疫科"></el-option>
              <el-option label="康复科" value="康复科"></el-option>
              <el-option label="中医科" value="中医科"></el-option>
              <el-option label="精神科" value="精神科"></el-option>
              <el-option label="传染科" value="传染科"></el-option>
              <el-option label="急诊科" value="急诊科"></el-option>
              <el-option label="其他" value="其他"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="治疗时长">
            <el-input v-model="nodeInfo.cureLasttime" placeholder="请输入治疗时长"></el-input>
          </el-form-item>
          <el-form-item label="治疗方式">
            <el-select v-model="nodeInfo.cureWay" multiple placeholder="请选择治疗方式">
              <el-option label="手术" value="手术"></el-option>
              <el-option label="药物" value="药物"></el-option>
              <el-option label="放疗" value="放疗"></el-option>
              <el-option label="化疗" value="化疗"></el-option>
              <el-option label="中药" value="中药"></el-option>
              <el-option label="其他" value="其他"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="治愈概率">
            <el-input v-model="nodeInfo.curedProb" placeholder="请输入治愈概率"
                      @blur="handleBlur">
            </el-input>
          </el-form-item>
          <el-form-item label="描述">
            <el-input v-model="nodeInfo.desc" placeholder="请输入描述" type="textarea" v-loading="isDescLoading" @blur="handleBlur2">
            </el-input>
          </el-form-item>
          <el-form-item label="易感人群">
            <el-input v-model="nodeInfo.easyGet" placeholder="请输入易感人群"></el-input>
          </el-form-item>
          <el-form-item label="预防措施">
            <el-input v-model="nodeInfo.prevent" placeholder="请输入预防措施"></el-input>
          </el-form-item>
        </el-form>

<!--        <el-form  v-if="nowActiveStep === 1"-->
<!--                  ref="dynamicValidateForm"-->
<!--                  label-width="100px">-->
<!--          <el-form-item label="节点1">-->
<!--            <el-input v-model="nodeInfo.name" disabled></el-input>-->
<!--          </el-form-item>-->
<!--          <el-form-item label="关系">-->
<!--            <el-select v-model="nodeInfo.relation" placeholder="请选择关系">-->
<!--              <el-option v-for="relation in relations" :key="relation" :label="relation" :value="relation"></el-option>-->
<!--            </el-select>-->
<!--          </el-form-item>-->
<!--          <el-form-item label="节点2"-->
<!--          >-->
<!--            <el-input v-model="dynamicRelationForm[0].targetNodeInfo.name"></el-input>-->
<!--            <el-button @click.prevent="removeDomain(domain)">删除</el-button>-->
<!--          </el-form-item>-->
<!--          <el-form-item>-->
<!--            <el-button type="primary" @click="submitForm('dynamicValidateForm')">提交</el-button>-->
<!--            <el-button @click="addDomain">新增域名</el-button>-->
<!--            <el-button @click="resetForm('dynamicValidateForm')">重置</el-button>-->
<!--          </el-form-item>-->
<!--        </el-form>-->
      </div>
      <span slot="footer" class="add-dialog-footer">
        <el-button @click="handleCancel">取 消</el-button>
        <el-button v-if="nowActiveStep <= 1"
                   type="primary"
                   @click="handleStepNext"
                   :loading="loading">下一步
        </el-button>
        <el-button v-else
                   type="primary"
                   @click="handleOk"
                   :loading="loading">确 定
        </el-button>
      </span>
    </el-dialog>

  </div>
</template>
<script>
import {UUID} from "@/utils/string";

export default {
  props: ['value'],
  name: 'AddDialog',
  data() {
    return {
      // 当前步骤
      nowActiveStep: 0,
      // 当前编辑的结点信息
      nodeInfo: {
        name: '',
        cause: '',
        cureDepartment: [],
        cureLasttime: '',
        cureWay: [],
        curedProb: '',
        category: '',
        desc: '',
        easyGet: '',
        prevent: '',
        uid: ''
      },
      // 结点关系列表
      relations:[
          "accompany_with",
          "belongs_to",
          "common_drug",
          "do_eat",
          "drugs_of",
          "has_symptom",
          "need_check",
          "no_eat",
          "recommend_drug",
          "recommend_eat",
      ],
      // 动态节点-关系节点表单数据
      dynamicRelationForm:[
        {
          sourceNodeInfo:{
            name: '',
            uid: ''
          },
          relation: '',
          targetNodeInfo:{
            name: '',
            uid: ''
          },
        }
      ],
      // 描述框联网编造
      isDescLoading: false,
      // 右下角确定键
      loading: false,
    };
  },
  methods: {
    // v-model 是一个语法糖，它实际上是 :value 和 @input 的简写
    closeModal() {
      this.$emit('input', false);
    },
    showModal() {
      this.$emit('input', true);
    },
    handleOk() {
      this.loading = true;
      this.$message({
        message: '正在上传，请稍候...',
        type: 'info'
      })
      setTimeout(() => {
        this.loading = false;
        this.$message({
          message: '上传成功！',
          type: 'success'
        });
        this.closeModal();
      }, 3000);
    },
    handleCancel(done) {
      this.$confirm('是否确认退出？退出将不保存任何内容！')
          .then(_ => {
            this.closeModal();
            done();
          })
          .catch(_ => {

          });
    },
    handleStepNext() {
      if (this.nowActiveStep++ > 1) this.nowActiveStep = 0;
    },
    handleBlur() {
      this.isDescLoading = true;
      setTimeout(() => {
        this.isDescLoading = false;
        this.nodeInfo.desc = "糖尿病是一种比较常见的内分泌代谢性疾病。该病发病原因主要是由于胰岛素分泌不足，以及胰升高血糖素不适当地分泌过多所引起。多见于40岁以上喜食甜食而肥胖的病人，城市多于农村，常有家族史，故与遗传有关。少数病人与病毒感染和自身免疫反应有关。主要表现为烦渴、多饮、多尿、多食、乏力、消瘦等症状。生命的常见病，伴发高血压、冠心病、高脂血症等，严重时危及生命。\n" +
            "中医学认为，肝主疏泄，关系人体接收机的升降与调畅，肝气郁滞则气机升降输布紊乱，肝失疏泄则血糖等精微物质不能随清阳之气输布于周身而郁滞于血中，出现高血糖或精微物质的输布紊乱，反见血糖升高，进一步导致血脂、蛋白等其它精微物质紊乱，引起其他合并症，治疗以疏肝调气为主，顺肝条达之性以恢复其生理功能，肝气条达，气机调畅，精微得以输布，糖被利用而血糖自然下降。\n" +
            "另外，因糖尿病的发生和饮食有关，饮食控制的好坏直接影响着治疗的效果。再就是配合运动，注意调摄情志，再适当的配合中药治疗会取得良好的治疗效果。\n"

      }, 4000);
    },
    handleBlur2() {
      this.isDescLoading = true;
      setTimeout(() => {
        this.isDescLoading = false;
        this.nodeInfo.desc = "糖尿病是一种常见的代谢紊乱疾病，主要特点是长期的高血糖。它可以分为两种类型：\n" +
            "1. 1型糖尿病：这是一种自身免疫性疾病，通常在儿童或青少年时期发病。在1型糖尿病中，身体的免疫系统错误地攻击并破坏胰腺中的胰岛细胞，这些细胞负责产生胰岛素。没有足够的胰岛素，身体不能有效地将血糖转化为能量，导致血糖水平升高。\n" +
            "2. 2型糖尿病：这是最常见的一种糖尿病，通常在成人中发病，尽管现在在儿童和青少年中也越来越常见。在2型糖尿病中，身体对胰岛素的抵抗性增加，或者胰腺不能产生足够的胰岛素。这同样导致血糖水平升高。\n" +
            "糖尿病的症状可能包括：\n" +
            "- 经常感到饥饿，尤其是在饭后不久\n" +
            "- 口渴增加\n" +
            "- 尿频，尤其是在夜间\n" +
            "- 疲劳和虚弱\n" +
            "- 体重下降，尽管食欲增加\n" +
            "- 视力模糊\n" +
            "- 刺痛、麻木或疼痛的皮肤，尤其是手和脚\n" +
            "如果糖尿病得不到控制，可能会导致一系列并发症，包括心脏病、中风、肾脏疾病、神经损伤、视力问题和皮肤问题。\n" +
            "治疗糖尿病的方法因人而异，但通常包括：\n" +
            "- 饮食和体重管理：通过均衡饮食和保持健康体重来控制血糖水平。\n" +
            "- 体育锻炼：定期锻炼可以帮助身体更有效地使用胰岛素，降低血糖水平。\n" +
            "- 药物治疗：包括口服药物和胰岛素注射，以帮助调节血糖水平。\n" +
            "- 血糖监测：定期检查血糖水平，以调整治疗方案。\n" +
            "- 教育和支持：了解糖尿病及其管理对于有效控制病情至关重要。\n" +
            "预防糖尿病的方法包括：\n" +
            "- 保持健康的生活方式，如均衡饮食和定期锻炼。\n" +
            "- 保持健康体重。\n" +
            "- 避免吸烟和过量饮酒。\n" +
            "- 定期进行健康检查，尤其是对于有糖尿病家族史的人。\n" +
            "糖尿病是一种慢性疾病，需要长期的管理和治疗。然而，通过适当的治疗和生活方式的改变，大多数糖尿病患者可以过上健康和积极的生活。\n"
      }, 3000);
    },

  },
  mounted() {
    this.nodeInfo.uid = UUID();
  }
};
</script>