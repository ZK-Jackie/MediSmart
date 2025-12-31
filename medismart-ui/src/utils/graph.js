/**
 * @param {Object} centerNode 中心结点
 * @param {Object} mapData 以中心结点为核心，周围点的数据
 * @returns {Object} 返回 echarts 关系图数据
 * */
const nameMapping = {
  "accompany_with": "伴随",
  "belongs_to": "属于",
  "common_drug": "常用药",
  "do_eat": "可食用",
  "drugs_of": "药品",
  "has_symptom": "症状",
  "need_check": "需检查",
  "no_eat": "忌食",
  "recommend_drug": "推荐药",
  "recommend_eat": "推荐食"
};

export function toEchartsData(centerNode, mapData) {
  let centerNodeDeepBak = JSON.parse(JSON.stringify(centerNode));
  const links = Object.keys(mapData);
  const nodes = Object.values(mapData);

  let retNodes = [];
  let retLinks = [];
  let retCategories = [];

  // 1. fill retNodes arr
  centerNodeDeepBak.symbolSize = 70;
  retNodes.push(centerNodeDeepBak);
  retCategories.push(centerNodeDeepBak.category);
  centerNodeDeepBak.category = retCategories.indexOf(centerNodeDeepBak.category);
  for (let i = 0; i < links.length; i++) {
    nodes[i].forEach((node, index) => {
      /** category **/
      // 判断当前结点的category是否已经存在于retCategories中，如果不存在则先添加再赋下标值，否则直接赋下标值
      if(!retCategories.includes(node.category)) {
        retCategories.push(node.category);
      }
        node.category = retCategories.indexOf(node.category);
      /** node **/
      // 确定大小，直接推送
      node.symbolSize = 50;
      retNodes.push(node);
      /** link **/
      // 梳理 推送
      retLinks.push({
        source: centerNodeDeepBak.id,
        target: node.id,
        name: links[i]
      })
    })
  }
  return {
    nodes: toStringUniqueNodes(retNodes),
    links: toStringLinks(retLinks),
    categories: toNameCategories(retCategories)
  }
}

function toStringUniqueNodes(nodes) {
  let unique = removeDuplicates(nodes);
  unique.forEach((item) => {
    item.id = item.id.toString();
  })
  // console.log(unique)
  return unique
}

function removeDuplicates(nodes) {
  return nodes.filter((item, index) => {
    return nodes.findIndex(obj => obj.id === item.id) === index;
  });
}

function toStringLinks(links){
  let tempLinks = [];

  links.forEach((item) => tempLinks.push({
    source: item.source.toString(),
    target: item.target.toString(),
    name: nameMapping[item.name]
    // name: item.name
  }))
  return tempLinks;
}

function toNameCategories(categories) {
  return categories.map((category, index) => {
    return {
      name: category
    }
  })
}