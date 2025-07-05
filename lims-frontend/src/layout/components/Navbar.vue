<template>
  <div class="navbar">
    <!--    <breadcrumb id="breadcrumb-container" class="breadcrumb-container" v-if="!topNav"/>-->
    <!--    <top-nav id="topmenu-container" class="topmenu-container" v-if="topNav"/>-->
    <div class="logo">
      <img src="@/assets/logo/ZTTlogo.png" />
    </div>
    <div class="center-menu">
      <span class="label">LIMS实验室管理系统</span>
    </div>
    <div class="right-menu">
      <div class="avatar-wrapper">
        <el-avatar shape="square" :size="30">{{ nickName.substring(0, 1) }}</el-avatar>
        <span class="userName">{{ nickName }}</span>
        <el-dropdown class="avatar-container right-menu-item hover-effect" trigger="click">
          <div class="avatar-wrapper">
            <i class="el-icon-caret-bottom" />
          </div>
          <el-dropdown-menu slot="dropdown">
            <router-link to="/user/profile">
              <el-dropdown-item>个人中心</el-dropdown-item>
            </router-link>
          </el-dropdown-menu>
        </el-dropdown>
        <img
          class="logoout"
          src="@/assets/images/logoout.png"
          @click="logout"
          title="退出账号"
        />
      </div>
      <!--      <template v-if="device!=='mobile'">-->
      <!--        <search id="header-search" class="right-menu-item" />-->

      <!--        <el-tooltip content="源码地址" effect="dark" placement="bottom">-->
      <!--          <ruo-yi-git id="ruoyi-git" class="right-menu-item hover-effect" />-->
      <!--        </el-tooltip>-->

      <!--        <el-tooltip content="文档地址" effect="dark" placement="bottom">-->
      <!--          <ruo-yi-doc id="ruoyi-doc" class="right-menu-item hover-effect" />-->
      <!--        </el-tooltip>-->

      <!--        <screenfull id="screenfull" class="right-menu-item hover-effect" />-->

      <!--        <el-tooltip content="布局大小" effect="dark" placement="bottom">-->
      <!--          <size-select id="size-select" class="right-menu-item hover-effect" />-->
      <!--        </el-tooltip>-->

      <!--      </template>-->

      <!--      <div class="avatar-container">-->
      <!--        <el-dropdown-menu slot="dropdown">-->
      <!--          <router-link to="/user/profile">-->
      <!--            <el-dropdown-item>个人中心</el-dropdown-item>-->
      <!--          </router-link>-->
      <!--          <el-dropdown-item @click.native="setting = true">-->
      <!--            <span>布局设置</span>-->
      <!--          </el-dropdown-item>-->
      <!--          <el-dropdown-item divided @click.native="logout">-->
      <!--            <span>退出登录</span>-->
      <!--          </el-dropdown-item>-->
      <!--        </el-dropdown-menu>-->
      <!--      </div>-->
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import Breadcrumb from "@/components/Breadcrumb";
import TopNav from "@/components/TopNav";
import Hamburger from "@/components/Hamburger";
import Screenfull from "@/components/Screenfull";
import SizeSelect from "@/components/SizeSelect";
import Search from "@/components/HeaderSearch";
import RuoYiGit from "@/components/RuoYi/Git";
import RuoYiDoc from "@/components/RuoYi/Doc";

export default {
  components: {
    Breadcrumb,
    TopNav,
    Hamburger,
    Screenfull,
    SizeSelect,
    Search,
    RuoYiGit,
    RuoYiDoc,
  },
  computed: {
    ...mapGetters(["device", "nickName"]),
    setting: {
      get() {
        return this.$store.state.settings.showSettings;
      },
      set(val) {
        this.$store.dispatch("settings/changeSetting", {
          key: "showSettings",
          value: val,
        });
      },
    },
    topNav: {
      get() {
        return this.$store.state.settings.topNav;
      },
    },
  },
  methods: {
    async logout() {
      this.$confirm("确定注销并退出系统吗？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.$store.dispatch("LogOut").then(() => {
            location.href = "/index";
          });
        })
        .catch(() => {});
    },
  },
};
</script>

<style lang="scss" scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  //position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  position: fixed; /* 将头部固定 */
  top: 0; /* 在顶部固定 */
  width: 100%; /* 宽度100%，覆盖整个视口 */
  //background-color: #f8f9fa; /* 设置背景颜色，以便更明显地看到效果 */
  z-index: 1000; /* 确保头部在其他内容之上 */
  display: flex;
  align-items: center;
  justify-content: space-between;

  .logo {
    width: 118px;
    height: 40px;
    img {
      width: 118px;
      height: 40px;
      margin-left: 20px;
      cursor: pointer;
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .topmenu-container {
    position: absolute;
    left: 50px;
  }

  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }
  .center-menu {
    float: left;
    height: 100%;
    line-height: 50px;

    .label {
      font-size: 14px;
    }
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;

    &:focus {
      outline: none;
    }
    .avatar-wrapper {
      display: flex;
      align-items: center;

      .user-avatar {
        cursor: pointer;
        width: 40px;
        height: 40px;
        border-radius: 10px;
      }
      .userName {
        font-size: 14px;
        margin-left: 10px;
      }
      .logoout {
        margin: 0 40px;
        cursor: pointer;
        width: 14px;
      }
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background 0.3s;

        &:hover {
          background: rgba(0, 0, 0, 0.025);
        }
      }
    }

  }
}
</style>
