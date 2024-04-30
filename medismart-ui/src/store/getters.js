const getters = {
  // user
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  name: state => state.user.name,
  roles: state => state.user.roles,
  introduction: state => state.user.introduction,
  // graph
  fuzzy: state => state.search.fuzzy,
  list: state => state.search.list,
  map: state => state.search.map,
}

export default getters;