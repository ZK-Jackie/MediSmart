import request from '@/utils/request'

// 搜索方法
export function fuzzySearch(keyword) {
  const data = {
    keyword: keyword,
    pageNow: 1,
    pageSize: 5
  }
  return request({
    url: '/kg/search',
    method: 'get',
    params: data
  })
}

// 详细列举方法
export function list(keyword) {
  const data = {
    keyword: keyword,
    pageNow: 1,
    pageSize: 5
  }
  return request({
    url: '/kg/list',
    method: 'get',
    params: data
  })
}

// 连接查询方法
export function map(id) {
  const data = {
    id: id
  }
  return request({
    url: '/kg/map',
    method: 'get',
    params: data
  })
}

// 修改结点信息方法
export function edit(node) {
  const req = {
    operation: 'update',
    nodeType: node.category,
    nodeInfo: node
  }
  // console.log(req);
  return request({
    url: '/kg/update',
    method: 'put',
    data: req
  })
}