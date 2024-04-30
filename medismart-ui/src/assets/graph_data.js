// 初始状态下，用户没搜索的时候展示的图表
export const graph =
  {
    "nodes": [
      {
        "id": "24",
        "name": "医疗知识图谱",
        "category": 2,
        "symbolSize": 100
      },
      {
        "id": "25",
        "name": "结点",
        "category": 0,
        "symbolSize": 70
      },
      {
        "id": "34",
        "name": "症状",
        "category": 1,
        "symbolSize": 50
      },
      {
        "id": "32",
        "name": "食物",
        "category": 1,
        "symbolSize": 50
      },
      {
        "id": "31",
        "name": "制药厂",
        "category": 1,
        "symbolSize": 50
      },
      {
        "id": "33",
        "name": "药物",
        "category": 1,
        "symbolSize": 50
      },
      {
        "id": "30",
        "name": "疾病",
        "category": 1,
        "symbolSize": 50
      },
      {
        "id": "29",
        "name": "科室",
        "category": 1,
        "symbolSize": 50
      },
      {
        "id": "28",
        "name": "检查",
        "category": 1,
        "symbolSize": 50
      },
      {
        "id": "27",
        "name": "关系",
        "category": 0,
        "symbolSize": 70
      },
      {
        "id": "37",
        "name": "常用药",
        "category": 1,
        "symbolSize": 50
      },
      {
        "id": "40",
        "name": "症状",
        "category": 1,
        "symbolSize": 50
      },
      {
        "id": "44",
        "name": "推荐食",
        "category": 1,
        "symbolSize": 50
      },
      {
        "id": "36",
        "name": "属于",
        "category": 1,
        "symbolSize": 50
      },
      {
        "id": "39",
        "name": "药品",
        "category": 1,
        "symbolSize": 50
      },
      {
        "id": "38",
        "name": "可食用",
        "category": 1,
        "symbolSize": 50
      },
      {
        "id": "41",
        "name": "需检查",
        "category": 1,
        "symbolSize": 50
      },
      {
        "id": "35",
        "name": "伴随",
        "category": 1,
        "symbolSize": 50
      },
      {
        "id": "43",
        "name": "推荐药",
        "category": 1,
        "symbolSize": 50
      },
      {
        "id": "42",
        "name": "忌食",
        "category": 1,
        "symbolSize": 50
      }
    ],
    "links": [
      {
        "source": "24",
        "target": "25",
        "name": "拥有"
      },
      {
        "source": "24",
        "target": "27",
        "name": "拥有"
      },
      {
        "source": "25",
        "target": "34",
        "name": "包含"
      },
      {
        "source": "25",
        "target": "32",
        "name": "包含"
      },
      {
        "source": "25",
        "target": "31",
        "name": "包含"
      },
      {
        "source": "25",
        "target": "33",
        "name": "包含"
      },
      {
        "source": "25",
        "target": "30",
        "name": "包含"
      },
      {
        "source": "25",
        "target": "29",
        "name": "包含"
      },
      {
        "source": "25",
        "target": "28",
        "name": "包含"
      },
      {
        "source": "27",
        "target": "37",
        "name": "包含"
      },
      {
        "source": "27",
        "target": "40",
        "name": "包含"
      },
      {
        "source": "27",
        "target": "44",
        "name": "包含"
      },
      {
        "source": "27",
        "target": "36",
        "name": "包含"
      },
      {
        "source": "27",
        "target": "39",
        "name": "包含"
      },
      {
        "source": "27",
        "target": "38",
        "name": "包含"
      },
      {
        "source": "27",
        "target": "41",
        "name": "包含"
      },
      {
        "source": "27",
        "target": "35",
        "name": "包含"
      },
      {
        "source": "27",
        "target": "43",
        "name": "包含"
      },
      {
        "source": "27",
        "target": "42",
        "name": "包含"
      }
    ],
    "categories": [
      {
        "name": "结点"
      },
      {
        "name": "医疗结点"
      },
      {
        "name": "医疗知识图谱"
      },
    ]
  }