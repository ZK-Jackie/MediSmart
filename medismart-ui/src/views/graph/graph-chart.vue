<script>
import {UUID} from "@/utils/string";
import {graph} from "@/assets/graph_data";

export default {
  name: 'Graph',
  props: {
    id: {
      type: String,
      required: false,
      default: UUID()
    },
    data: {
      type: Object,
      required: true
    },
  },
  watch: {
    data: {
      handler: function () {
        this.loadChart(this.data, false);
      },
      deep: true
    }
  },
  data() {
    return {
      chartDOM: null,
    };
  },
  mounted() {
    this.$echarts.then(
        echarts => {
          this.chartDOM = echarts.init(document.getElementById('chart-item-map-' + this.id));
          this.loadChart(graph);
        }
    )
  },
  methods: {
    loadChart(chartData, isSpin = true) {
      let categoryMap = chartData.categories;
      // console.log(categoryMap);
      const option = {
        title: {
          text: '医智通(MediSmart)知识库',
          subtext: 'Default layout',
          top: 'bottom',
          left: 'right'
        },
        tooltip: {
          formatter: function (params) {
            if (params.dataType === 'node') {
              // 对于节点，第一行展示节点的 names 属性，第二行展示小圆点和小圆点的属性名字
              try {
                return params.data.name + '<br/>' + params.marker + categoryMap[params.data.category].name;
              } catch (e) {
                console.error(e);
                return params.data.name + '<br/>' + params.marker;
              }
            } else if (params.dataType === 'edge') {
              // 对于边，第一行展示【起始节点name属性名字】【边的name属性名字】【终止节点的name属性名字】
              let sourceNode = chartData.nodes.find(node => node.id == params.data.source);
              let targetNode = chartData.nodes.find(node => node.id == params.data.target);
              return sourceNode.name + " " + params.data.name + " " + targetNode.name;
            }
          }
        },
        legend: [
          {
            data: chartData.categories.map(function (a) {
              return a;
            }),
            inactiveColor: 'rgba(0,0,0,.2)',  // 未激活时的颜色
            textStyle: {
              color: "rgba(0,0,0,.6)"  // 激活时的颜色
            },
            fontSize: 12,
          }
        ],
        animationDuration: 500,
        animationDurationUpdate: 500,
        animationEasingUpdate: 'quinticInOut',
        series: [
          {
            name: '医疗信息结点',
            type: 'graph',
            layout: 'force',
            force: {
              repulsion: 1000,
              edgeLength: 5,
              layoutAnimation: isSpin,
            },
            lineStyle: {
              color: 'target',
              curveness: 0.3
            },
            data: chartData.nodes,
            links: chartData.links,
            categories: chartData.categories,
            roam: true,
            label: {
              show: true,
              position: 'inside',
              color: 'rgba(0,0,0,.6)',
              formatter: function (params) {
                let name = params.data.name;
                let formattedName = '';
                let lineCount = 0;
                for (let i = 0; i < name.length; i++) {
                  if (i > 0 && i % 5 === 0) {
                    lineCount++;
                    if (lineCount >= 2) {
                      formattedName += '...';
                      break;
                    }
                    formattedName += '\n';
                  }
                  formattedName += name[i];
                }
                return formattedName;
              }
            },
            edgeLabel: {
              show: true,  // 始终显示边的标签
              position: 'middle',  // 边的标签位置
              formatter: function (params) {
                return params.data.name;  // 显示边的 name 属性
              },
              color: 'rgba(0,0,0,.6)'  // 边的标签颜色
            },
            emphasis: {
              focus: 'adjacency',
              lineStyle: {
                width: 10
              }
            }
          }
        ]
      }
      this.chartDOM && this.chartDOM.setOption(option);
    },
  },
};
</script>

<template>
  <div class="chart-item-map"
       :id="'chart-item-map-'+ id"
       style="width: 100%; height: 100%;"
  ></div>
</template>