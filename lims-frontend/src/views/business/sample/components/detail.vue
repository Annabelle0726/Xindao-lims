<template>
  <div style="width: 100%;height: 100%;overflow-y: auto;" class="detail">
    <div>
      <el-row class="title">
        <el-col :span="12" style="padding-left: 20px;text-align: left;">样品详情</el-col>
        <el-col :span="12" style="text-align: right;">
          <el-button size="small" @click="hanldeBack">返回</el-button>
        </el-col>
      </el-row>
    </div>
    <div class="search">
      <div class="search_thing">
        <div class="search_label">样品编号：</div>
        <div class="search_input">
          <el-input size="small" placeholder="请输入" clearable
							v-model="entity.sampleCode" disabled></el-input>
        </div>
      </div>
      <div class="search_thing">
        <div class="search_label">样品名称：</div>
        <div class="search_input">
          <el-input size="small" placeholder="请输入" clearable
							v-model="entity.sample" disabled></el-input>
        </div>
      </div>
      <div class="search_thing">
        <div class="search_label">样品数量：</div>
        <div class="search_input">
          <el-input size="small" placeholder="请输入" clearable
							v-model="entity.num" disabled></el-input>
        </div>
      </div>
      <div class="search_thing">
        <div class="search_label">单位：</div>
        <div class="search_input">
          <el-input size="small" placeholder="请输入" clearable
							v-model="entity.unit" disabled></el-input>
        </div>
      </div>
      <div class="search_thing">
        <div class="search_label">入库时间：</div>
        <div class="search_input">
          <el-input size="small" placeholder="请输入" clearable
							v-model="entity.date" disabled></el-input>
        </div>
      </div>
      <div class="search_thing">
        <div class="search_label">入库人：</div>
        <div class="search_input">
          <el-input size="small" placeholder="请输入" clearable
							v-model="entity.user" disabled></el-input>
        </div>
      </div>
      <div class="search_thing" >
        <div class="search_label">库位号：</div>
        <div class="search_input">
          <el-input size="small" placeholder="请输入" clearable
							v-model="entity.code" disabled style="min-width: 230px;"></el-input>
        </div>
      </div>
    </div>
    <h4>检验项目</h4>
    <div class="table">
      <el-table class="el-table" ref="productTable" :data="products" height="380px" tooltip-effect="dark"
                :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border>
				<el-table-column type="index" align="center" label="序号" width="70" :key="Math.random()">
				</el-table-column>
				<el-table-column prop="inspectionItem" label="检验项" min-width="100"
					show-overflow-tooltip></el-table-column>
				<el-table-column prop="inspectionItemSubclass" label="检验项分类" min-width="100" show-overflow-tooltip v-if="PROJECT=='装备电缆'"></el-table-column>
				<el-table-column prop="laboratory" label="实验室" min-min-width="100" show-overflow-tooltip></el-table-column>
				<el-table-column prop="unit" label="单位" min-width="100" show-overflow-tooltip></el-table-column>
				<el-table-column prop="equipValue" label="设备" min-width="100" show-overflow-tooltip>
          <template slot-scope="scope">
            {{handleData(scope.row.equipValue)}}
          </template>
        </el-table-column>
				<el-table-column prop="entrustCode" label="委托编号" min-width="100" show-overflow-tooltip></el-table-column>
				<el-table-column prop="updateUserName" label="检验人" min-width="100" show-overflow-tooltip></el-table-column>
				<el-table-column prop="updateTime" label="检验时间" min-width="100" show-overflow-tooltip></el-table-column>
				<el-table-column prop="insResult" label="结论" min-width="100">
					<template slot-scope="scope">
            <el-tag
              :type="scope.row.insResult==1?'success':'danger'" size="medium">{{scope.row.insResult==1?'合格':'不合格'}}</el-tag>
					</template>
				</el-table-column>
			</el-table>
    </div>
    <h4>出入库历史</h4>
    <div class="table" style="margin-bottom: 20px;">
      <el-table class="el-table" ref="productTable" :data="histories" height="380px" tooltip-effect="dark"
                :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border>
				<el-table-column type="index" align="center" label="序号" width="70" :key="Math.random()">
				</el-table-column>
				<el-table-column prop="state" label="类型"
					show-overflow-tooltip>
          <template slot-scope="scope">
            <el-tag :type="scope.row.state==1?'success':'danger'">{{ scope.row.state==1?'入库':'出库' }}</el-tag>
          </template>
        </el-table-column>
				<el-table-column prop="createUserName" label="操作人"  show-overflow-tooltip></el-table-column>
				<el-table-column prop="createTime" label="操作时间"  show-overflow-tooltip></el-table-column>
				<el-table-column prop="warehouseCode" label="库位号" show-overflow-tooltip></el-table-column>
				<!-- <el-table-column prop="price" label="存放周期（h）" show-overflow-tooltip></el-table-column> -->
			</el-table>
    </div>
  </div>
</template>

<script>
export default {
  props:['id'],
  data(vm) {
      return{
        entity:{
          num:1,
        },
        products:[],
        histories:[]
      }
  },
  mounted(){
    this.getInfo()
  },
  methods:{
    hanldeBack(){
      this.$emit('hanldeBack')
    },
    getInfo(){
      this.$axios.post(this.$api.warehouse.getSampleRecord, {
        id: this.id
      }).then(res => {
        let {histories,insSample,sampleHistory,products} = res.data;
        this.entity = {num:1,...insSample,...sampleHistory};
        this.histories = histories;
        this.products = products;
      })
    },
    handleData(e){
      let info = ''
      if(e){
        info = JSON.parse(e).map(item => {
        return item.v;
      }).join(',')
      }
      return info
    }
  }
}
</script>

<style scoped>
	.detail::-webkit-scrollbar{
		width: 0;
	}

	.title {
		height: 60px;
		line-height: 60px;
	}

	.search {
		background-color: #fff;
		height: 120px;
		display: flex;
		align-items: center;
    flex-wrap: wrap;
	}

	.search_thing {
		display: flex;
		align-items: center;
		height: 40px;
	}

	.search_label {
		width: 120px;
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
		padding: 20px;
	}
  h4{
    font-size: 16px;
    font-weight: normal;
    margin-top: 10px;
    margin-left: 20px;
  }

</style>
