import request from '@/utils/request'

// export function login(data) {
//   return request({
//     url: '/user/login',
//     method: 'post',

// post可以传json格式

//     data
//   })
// } 

// 多个查询方法放在一起
export default {
  getUserList(searchModel) {
    return request({
      url: '/user/getUserList',
      method: 'get',
      // get请求传不了json，所以要自定义参数
      params: {
        pageNo: searchModel.pageNo,
        pageSize: searchModel.pageSize,
        username: searchModel.username,
        phone: searchModel.phone
      }
    })
  },
  addUser(user) {
    return request({
      url: '/user/addUser',
      method: 'post',
      data: user
    })
  },
  saveUser(user) {
    if (user.id == undefined && user.id == null) return this.addUser(user)
    else return this.updateUser(user)
  },
  updateUser(user) {
    return request({
      url: '/user/updateUser',
      method: 'put',
      data: user
    })
  },
  getUserById(id) {
    return request({
      // 注意不是单引号
      url: `/user/${id}`,
      method: 'get',
    })
  },
  deleteUserById(id) {
    return request({
      // 注意不是单引号
      url: `/user/${id}`,
      method: 'delete',
    })
  },
}
