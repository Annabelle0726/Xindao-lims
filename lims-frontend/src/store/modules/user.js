import { login, logout, getInfo, LoginBySSO } from "@/api/login";
import { getToken, setToken, removeToken } from "@/utils/auth";
import { isHttp, isEmpty } from "@/utils/validate";
import defAva from "@/assets/images/profile.jpg";
import Vue from "vue";

const user = {
  state: {
    token: getToken(),
    id: "",
    name: "",
    avatar: "",
    nickName: "",
    nameEn: "",
    userName: "",
    roles: [],
    permissions: [],
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token;
    },
    SET_ID: (state, id) => {
      state.id = id;
    },
    SET_NAME: (state, name) => {
      state.name = name;
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar;
    },
    SET_NICKNAME: (state, nickName) => {
      state.nickName = nickName;
    },
    SET_NAMEEN: (state, nameEn) => {
      state.nameEn = nameEn;
    },
    SET_USERNAME: (state, userName) => {
      state.userName = userName;
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles;
    },
    SET_PERMISSIONS: (state, permissions) => {
      state.permissions = permissions;
    },
  },

  actions: {
    // 登录
    Login({ commit }, userInfo) {
      const username = userInfo.username.trim();
      const password = userInfo.password;
      const code = userInfo.code;
      const uuid = userInfo.uuid;
      return new Promise((resolve, reject) => {
        login(username, password, code, uuid)
          .then((res) => {
            setToken(res.token);
            commit("SET_TOKEN", res.token);
            Vue.prototype.uploadHeader = {
              Authorization: "Bearer " + res.token,
            };
            resolve();
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    // 单点登录
    LoginBySSO({ commit }, accessToken) {
      return new Promise((resolve, reject) => {
        LoginBySSO(accessToken)
          .then((res) => {
            setToken(res.token);
            commit("SET_TOKEN", res.token);
            Vue.prototype.uploadHeader = {
              Authorization: "Bearer " + res.token,
            };
            resolve();
          })
          .catch((error) => {
            reject(error);
          });
      })
    },
    // 获取用户信息
    GetInfo({ commit, state }) {
      return new Promise((resolve, reject) => {
        getInfo()
          .then((res) => {
            const user = res.user;
            let avatar = user.avatar || "";
            if (!isHttp(avatar)) {
              avatar = isEmpty(avatar)
                ? defAva
                : process.env.VUE_APP_BASE_API + avatar;
            }
            if (res.roles && res.roles.length > 0) {
              commit("SET_ROLES", res.roles);
              commit("SET_PERMISSIONS", res.permissions);
            } else {
              commit("SET_ROLES", ["ROLE_DEFAULT"]);
            }
            commit("SET_ID", user.userId);
            commit("SET_NAME", user.userName);
            commit("SET_AVATAR", avatar);
            commit("SET_NICKNAME", user.nickName);
            commit("SET_NAMEEN", user.nameEn);
            commit("SET_USERNAME", user.userName);
            resolve(res);
          })
          .catch((error) => {
            reject(error);
          });
      });
    },

    // 退出系统
    LogOut({ commit, state }) {
      return new Promise((resolve, reject) => {
        logout(state.token)
          .then((res) => {
            if (res.data) {
              window.location.href = res.data
            } else {
              commit("SET_TOKEN", "");
              commit("SET_ROLES", []);
              commit("SET_PERMISSIONS", []);
              removeToken();
              resolve();
            }
          })
          .catch((error) => {
            reject(error);
          });
      });
    },

    // 前端 登出
    FedLogOut({ commit }) {
      return new Promise((resolve) => {
        commit("SET_TOKEN", "");
        removeToken();
        resolve();
      });
    }
  }, // end actions

  getters: {
    isAuthenticated: state => !!state.token,
    currentUser: state => ({
      id: state.id,
      name: state.name,
      nickName: state.nickName,
      avatar: state.avatar,
      roles: state.roles,
      permissions: state.permissions
    }),

  }
};

export default user;
