<template>
  <div class="capacity-scope">
    <div v-if="!isDetail">
      <div style="display: flex;justify-content: space-between">
        <el-form :model="entity" ref="entity" size="small" :inline="true">
          <el-form-item label="仓库名称" prop="warehouseId">
            <el-select v-model="entity.warehouseId" placeholder="选择仓库" size="small" @change="warehouseChange">
              <el-option v-for="item in warehouse" :key="item.id" :label="item.name" :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="货架" prop="shelfId">
            <el-select v-model="entity.shelfId" placeholder="选择货架" size="small" @change="handleShelf">
              <el-option v-for="item in shelf" :key="item.id" :label="item.name" :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="样品编号" prop="searchSampleCode">
            <el-input placeholder="请输入样品编号" v-model="searchSampleCode" size="small">
              <el-button slot="append" icon="el-icon-search" @click="handleSearch"></el-button>
            </el-input>
          </el-form-item>
          <el-form-item>
            <el-button size="small" style="color:#3A7BFA" @click="keepVisible=true">维护</el-button>
            <el-button size="small" style="color:#3A7BFA" @click="warehouseVisible=true,isEdit=false">添加仓库</el-button>
            <el-button size="small" style="color:#3A7BFA" @click="shelvesVisible=true,isEdit=false"
                       :disabled="entity.warehouseId==null">添加货架</el-button>
          </el-form-item>
        </el-form>
        <div>
          <el-button size="small" type="primary" @click="storageVisible=true">入库</el-button>
          <el-button size="small" type="primary" @click="exportVisible=true">出库</el-button>
        </div>
      </div>
      <div class="table" v-loading="tableLoading">
        <table border="1" class="tables" cellpadding="10" style="table-layout:fixed;" v-if="tableList.length>0">
          <tbody>
          <tr v-for="(item,index) in tableList" :key="index">
            <td v-for="(m,i) in item" :key="i" class="content">
              <h4 v-if="m.row!=undefined">{{ m.row }} - {{ m.col }}</h4>
              <ul>
                <el-tooltip
                  effect="dark"
                  placement="top"
                  v-for="(n,j) in m.samples"
                  :key="j" :disabled="`${n.sample}${n.model}`.length<10">
                  <div slot="content"><span>{{ n.sample }}</span>
                    <span>&nbsp;({{ n.model }})&nbsp;[{{ n.sampleCode }}]</span></div>
                  <li class="green"
                      @click="handelDetail(n)">
                    <i></i>
                    <span>{{ n.sample }}</span>
                    <span>&nbsp;({{ n.model }})&nbsp;[{{ n.sampleCode }}]</span>
                    <!-- <span class="num">&nbsp;x{{ n.num }}</span> -->
                  </li>
                </el-tooltip>
              </ul>
            </td>
          </tr>
          <tr>
            <td v-for="(item,index) in rowList" :key="index" style="background: ghostwhite;height: 20px;">{{ item }}
            </td>
          </tr>
          </tbody>
        </table>
        <span v-else style="color: rgb(144, 147, 153);display: inline-block;position: absolute;top: 60%;left: 50%;transform: translate(-50%,-50%);">暂无数据</span>
      </div>
    </div>
    <Detail v-else @hanldeBack="isDetail=false" :id="currentId" />
    <el-dialog title="样品入库" :visible.sync="storageVisible" width="350px">
      <el-row>
        <el-col class="search_thing" :span="24">
          <div class="search_label"><span class="required-span">* </span>样品编号：</div>
          <div class="search_input">
            <el-input v-model="sampleCode" size="small"></el-input>
          </div>
        </el-col>
      </el-row>
      <el-tree :data="storageList" ref="tree" :props="{ children: 'warehouseShelfList', label: 'name' }" node-key="id"
               :filter-node-method="filterNode" @node-click="handleNodeClick" highlight-current @node-expand="nodeOpen"
               @node-collapse="nodeClose" :default-expanded-keys="expandedKeys" v-if="storageVisible"
               empty-text="暂无数据">
        <div class="custom-tree-node" slot-scope="{ node, data }">
          <el-row>
            <el-col :span="24">
              <span><i
                :class="`node_i ${data.warehouseShelfList != undefined ? 'el-icon-folder-opened' : 'el-icon-tickets'}`"></i>
                {{ data.name }}</span>
            </el-col>
          </el-row>
        </div>
      </el-tree>
      <span slot="footer" class="dialog-footer">
        <el-button @click="storageVisible = false">取 消</el-button>
        <el-button type="primary" @click="confirmStorage" :loading="upLoadStorage">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog title="样品出库" :visible.sync="exportVisible" width="350px">
      <!-- <div class="shaoma">
        <img src="../../../static/img/扫码.svg" alt="" style="margin-right: 5px;">
        <span>扫码出库</span>
      </div> -->
      <el-row>
        <el-col class="search_thing" :span="24">
          <div class="search_label"><span class="required-span">* </span>样品编号：</div>
          <div class="search_input">
            <el-input v-model="sampleCode" size="small"></el-input>
          </div>
        </el-col>
      </el-row>
      <span slot="footer" class="dialog-footer">
        <el-button @click="exportVisible = false">取 消</el-button>
        <el-button type="primary" @click="confirmExport" :loading="upLoadExport">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog title="库位维护" :visible.sync="keepVisible" width="350px" :append-to-body="true">
      <el-tree :data="warehouse" ref="tree" :props="{ children: 'warehouseShelfList', label: 'name' }" node-key="id"
               :filter-node-method="filterNode" @node-click="handleNodeClick" highlight-current @node-expand="nodeOpen"
               @node-collapse="nodeClose" :default-expanded-keys="expandedKeys" v-if="keepVisible"
               empty-text="暂无数据">
        <div class="custom-tree-node" style="width: 100%;" slot-scope="{ node, data }">
          <el-row style="width: 100%;display: flex;align-items: center;">
            <el-col :span="20">
              <span><i
                :class="`node_i ${node.level<2 ? 'el-icon-folder-opened': 'el-icon-tickets'}`"></i>
                {{ data.name }}</span>
            </el-col>
            <el-col :span="4" v-if="node.level<3">
              <el-button type="text" size="mini" icon="el-icon-edit" @click.stop="handleEdit(data,node.level)">
              </el-button>
              <el-button type="text" size="mini" icon="el-icon-delete" @click.stop="handleDelete(data,node.level)">
              </el-button>
            </el-col>
          </el-row>
        </div>
      </el-tree>
      <span slot="footer" class="dialog-footer">
        <el-button @click="keepVisible = false">取 消</el-button>
        <el-button type="primary" @click="keepVisible = false" >确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog :title="isEdit?'仓库修改':'仓库新增'" :visible.sync="warehouseVisible" width="350px">
      <el-row>
        <el-col class="search_thing" :span="24">
          <div class="search_label"><span class="required-span">* </span>仓库名称：</div>
          <div class="search_input">
            <el-input v-model="name" size="small" @keyup.enter.native="confirmWarehouse"></el-input>
          </div>
        </el-col>
      </el-row>
      <span slot="footer" class="dialog-footer">
        <el-button @click="warehouseVisible = false">取 消</el-button>
        <el-button type="primary" @click="confirmWarehouse" :loading="upLoadWarehouse">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog :title="isEdit?'货架修改':'货架新增'" :visible.sync="shelvesVisible" width="350px">
      <el-row>
        <el-col class="search_thing" :span="24">
          <div class="search_label"><span class="required-span">* </span>货架名称：</div>
          <div class="search_input">
            <el-input v-model="shelves.name" size="small"></el-input>
          </div>
        </el-col>
      </el-row>
      <el-row>
        <el-col class="search_thing" :span="24">
          <div class="search_label"><span class="required-span">* </span>货架层数：</div>
          <div class="search_input">
            <el-input v-model="shelves.row" size="small"></el-input>
          </div>
        </el-col>
      </el-row>
      <el-row>
        <el-col class="search_thing" :span="24">
          <div class="search_label"><span class="required-span">* </span>货架列数：</div>
          <div class="search_input">
            <el-input v-model="shelves.col" size="small"></el-input>
          </div>
        </el-col>
      </el-row>
      <span slot="footer" class="dialog-footer">
        <el-button @click="shelvesVisible = false">取 消</el-button>
        <el-button type="primary" @click="confirmShelves" :loading="upLoadShelves">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import Detail from './components/detail.vue'
import {
  addShelf,
  addWarehouse, delShelf, delWarehouse, getWarehouse,
  inWarehouse,
  outWarehouse, searchSampleId,
  selectWarehouse,
  upShelf,
  upWarehouse
} from "@/api/business/sample";
export default {
  name: 'Sample',
  components: {
    Detail
  },
  data() {
    return {
      entity: {
        warehouseId: null,
        shelfId: null
      },
      warehouse: [],
      shelf: [],
      storageList:[],
      storageVisible: false,
      exportVisible: false,
      keepVisible: false,
      warehouseVisible: false,
      shelvesVisible: false,
      upLoadStorage: false,
      upLoadExport: false,
      upLoadWarehouse: false,
      upLoadShelves: false,
      tableList: [],
      rowList: [],
      value: '',
      name:'',
      shelves:{},
      selectTree: [],
      expandedKeys: [],
      isEdit: false, //弹框--是否是修改,默认为false
      isDetail: false, //详情--是否展示为详情页面，默认为false
      currentEdit:null,//当前要维护的信息
      tableLoading:false,
      sampleCode:'',
      currentId:null,
      searchSampleCode:''
    }
  },
  mounted() {
    this.selectList()
  },
  methods: {
    selectList() {
      selectWarehouse().then(res => {
        this.warehouse = res.data
        if(res.data.length==0){
          this.entity.warehouseId = '';
          this.entity.shelfId = '';
          this.tableList = []
        }
        this.storageList = this.HaveJson(res.data).map(item=>{
          item.warehouseShelfList&&item.warehouseShelfList.length>0&&item.warehouseShelfList.map(m=>{
            let warehouseShelfList = [];
            for (let i=1;i<m.row+1;i++){
              let warehouseShelfList0 = []
              for (let j=1;j<m.col+1;j++){
                warehouseShelfList0.push({
                  name:j+' 列',
                  id:j,
                })
              }
              warehouseShelfList.push({
                name:i+' 层',
                id:i,
                warehouseShelfList:warehouseShelfList0
              })
            }
            m.warehouseShelfList = warehouseShelfList;
            return m;
          })
          return item;
        })
        if(!this.entity.warehouseId&&this.warehouse.length>0){
          this.entity.warehouseId = this.warehouse[0].id
          this.warehouseChange(this.entity.warehouseId)
          if(this.shelf.length>0){
            this.entity.shelfId = this.shelf[0].id
            this.handleShelf(this.entity.shelfId)
          }else{
            this.tableList = []
          }
        }else if(this.warehouse.length>0){
          this.warehouseChange(this.entity.warehouseId)
          if(this.shelf.length>0){
            this.entity.shelfId = this.shelf[0].id
            this.handleShelf(this.entity.shelfId)
          }else{
            this.tableList = []
          }
        }
      })
    },
    // 入库
    confirmStorage() {
      if (!this.sampleCode) {
        this.$message.error('请填写样品编号')
        return
      }
      if (this.selectTree.length < 4) {
        this.$message.error('请选择样品入库位置')
        return
      }
      this.upLoadStorage = true;
      inWarehouse({
        trees: this.selectTree.join('-'),
        sampleCode:this.sampleCode
      }).then(res => {
        this.upLoadStorage = false;
        this.storageVisible = false
        this.sampleCode = '';
        this.selectTree = []
        this.$message.success('入库成功')
        this.handleShelf(this.entity.shelfId)
      }).catch(err => {
        this.upLoadStorage = false;
      })
    },
    // 出库
    confirmExport() {
      if (!this.sampleCode) {
        this.$message.error('请填写样品编号')
        return
      }
      this.upLoadExport = true;
      outWarehouse({
        sampleCode:this.sampleCode
      }).then(res => {
        this.upLoadExport = false;
        this.exportVisible = false
        this.sampleCode = '';
        this.$message.success('出库成功')
        this.handleShelf(this.entity.shelfId)
      }).catch(err => {
        this.upLoadExport = false;
      })
    },
    // 添加/修改仓库
    confirmWarehouse() {
      if (!this.name) {
        this.$message.error('请填写仓库名称')
        return
      }
      this.upLoadWarehouse = true;
      if(this.currentEdit&&this.currentEdit.id){
        // 修改仓库
        upWarehouse({
          name: this.name,
          id:this.currentEdit.id
        }).then(res => {
          this.upLoadWarehouse = false;
          this.warehouseVisible = false
          // this.keepVisible = false
          this.currentEdit = null;
          this.$message.success('修改成功')
          this.selectList()
          this.name = ''
          this.warehouseChange(this.entity.warehouseId)
        }).catch(err => {
          this.upLoadWarehouse = false;
        })
      }else{
        // 新增仓库
        addWarehouse({
          name: this.name
        }).then(res => {
          this.upLoadWarehouse = false;
          this.warehouseVisible = false
          this.$message.success('添加成功')
          this.selectList()
          this.name = ''
          this.warehouseChange(this.entity.warehouseId)
        }).catch(err => {
          this.upLoadWarehouse = false;
        })
      }
    },
    // 添加/修改货架
    confirmShelves() {
      if (!this.shelves.name) {
        this.$message.error('请填写货架名称')
        return
      }
      if (!this.shelves.row) {
        this.$message.error('请填写货架层数')
        return
      }
      if (!this.shelves.col) {
        this.$message.error('请填写货架列数')
        return
      }
      this.upLoadShelves = true;
      if(this.currentEdit&&this.currentEdit.id){
        // 修改
        upShelf({
          id:this.currentEdit.id,...this.shelves
        }).then(res => {
          this.upLoadShelves = false;
          this.shelvesVisible = false
          this.$message.success('修改成功')
          this.selectList()
          this.currentEdit ={};
        }).catch(err => {
          this.upLoadShelves = false;
        })
      }else{
        // 新增
        addShelf({
          warehouseId: this.entity.warehouseId,...this.shelves
        }).then(res => {
          this.upLoadShelves = false;
          this.shelvesVisible = false
          this.$message.success('添加成功')
          this.selectList()
          this.shelves = {}
        }).catch(err => {
          this.upLoadShelves = false;
        })
      }
      this.warehouseChange(this.entity.warehouseId)
    },
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    handleNodeClick(val, node, el) { //树的值
      this.selectTree[node.level-1] = val.id;
    },
    nodeOpen(data, node, el) {
      $($(el.$el).find('.node_i')[0]).attr('class', 'node_i el-icon-folder-opened')
      this.selectTree[node.level-1] = data.id;
    },
    nodeClose(data, node, el) {
      $($(el.$el).find('.node_i')[0]).attr('class', 'node_i el-icon-folder')
    },
    handleDelete(row, level) {
      this.$confirm('是否当前数据?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        if (level == 1) {
          // 删除仓库
          delWarehouse({
            id: row.id
          }).then(res => {
            this.$message.success('成功')
            this.selectList()
          }).catch(e => {
            this.$message.error('失败')
          })
        }else{
          // 删除货架
          delShelf({
            id: row.id
          }).then(res => {
            this.$message.success('成功')
            this.selectList()
          }).catch(e => {
            this.$message.error('失败')
          })
        }
        this.warehouseChange(this.entity.warehouseId)
      }).catch(() => {})
    },
    handleEdit(data, level) {
      this.isEdit = true;
      // 判断是第几层级，第一层级，修改仓库，第二层级修改货架
      if (level == 1) {
        this.warehouseVisible = true;
        this.currentEdit = data;
        this.name = data.name
      } else {
        this.shelvesVisible = true;
        this.currentEdit = data;
        this.shelves = {
          name:data.name,
          row:data.row,
          col:data.col,
          warehouseId:data.warehouseId
        }
      }
    },
    // 查看详情
    handelDetail(row) {
      this.currentId = row.id;
      this.isDetail = true;
    },
    warehouseChange(val) {
      this.tableList = []
      let map = this.warehouse.find(a => {
        return a.id === val ? a : null
      })
      this.shelf = map.warehouseShelfList;
      this.entity.shelfId = '';
    },
    handleShelf(e){
      if(e){
        this.tableLoading = true;
        getWarehouse({shelfId: e}).then(res => {
          this.tableLoading = false;
          let data = res.data;
          let set =new Set()
          this.tableList = [];
          let arr = []
          data.forEach(m=>{
            set.add(m.col)
            if(arr.length>0){
              if(arr.find(n=>n.row==m.row)){
                arr.push(m)
              }else{
                this.tableList.push(arr)
                arr = []
                arr.push(m)
              }
            }else{
              arr.push(m)
            }
          })
          this.tableList.push(arr)
          this.rowList = [];
          for(let i=0;i<set.size;i++){
            this.rowList.push(`${i+1} 列`)
          }
        }).catch(e=>{
          this.tableLoading = false;
        })
      }
    },
    handleSearch(){
      if(!this.searchSampleCode){
        this.$message.error('请输入样品编号')
        return;
      }
      searchSampleId({sampleCode: this.searchSampleCode}).then(res => {
        if(res.code==200){
          this.currentId = res.data;
          this.isDetail = true;
        }
      })
    }
  }
}
</script>
<style scoped>
.search_thing {
  display: flex;
  align-items: center;
  height: 50px;
}

.search_label {
  width: 90px;
  font-size: 14px;
  text-align: right;
}

.search_input {
  width: calc(100% - 120px);
}

.table {
  margin-top: 10px;
  background-color: #fff;
  width: calc(100% - 40px);
  height: calc(100% - 60px - 80px - 10px - 40px);
  padding: 20px;
  overflow-y: auto;
}

.tables {
  width: 100%;
  height: 100%;
  border-bottom: none;
}

.tables th {
  font-size: 14px;
}

.tables td {
  font-size: 12px;
  text-align: center;
  vertical-align: top;
  border-color: rgb(192, 191, 191) !important;
  padding: 5px;
  box-sizing: border-box;
  height: 120px;
}

.tables ul {
  list-style-type: none;
}

.tables ul li {
  border-radius: 3px;
  padding: 4px 10px;
  box-sizing: border-box;
  margin-bottom: 5px;
  font-size: 12px;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  color: #333333;
  cursor: pointer;
  overflow: hidden; /*超出部分隐藏*/
  white-space: nowrap; /*禁止换行*/
  text-overflow: ellipsis; /*省略号*/
}

.tables h4 {
  color: #999999;
  font-size: 14px;
  font-weight: 400;
  padding: 6px 0;
}

.tables i {
  display: inline-block;
  width: 6px;
  height: 6px;
  border-radius: 50%;
  margin-right: 6px;
}

li:hover {
  background: rgba(58, 123, 250, 0.18);
}

li:hover i {
  background: #3A7BFA;
}

li:hover .num {
  color: #3A7BFA;
}

.green {
  background: #E0F6EA;
}

.green i {
  background: #34BD66;
}

.green .num {
  color: #34BD66;
}

.custom-tree-node .el-button {
  opacity: 0;
}

.custom-tree-node:hover .el-button {
  opacity: 1;
}
>>>.el-loading-mask {
  z-index: 10;
}
</style>
