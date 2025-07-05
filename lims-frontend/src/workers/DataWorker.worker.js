// 此线程为数据采集线程

// 保存数据
// 数据采集信息
let dataAcquisitionInfo = null;
// 数据列表
let list = null;
// 传递到主线程的数据
let result = {
  method:'',
  value:null
}
// 优化数采较多数据时--记录数采项目的Id数组，目的是取最后一个数采项目，传到主线程进行批量的数据采集保存
let arrSpecial = []
// 接收主线程传递过来的数据
self.onmessage = function(event) {
  const data = JSON.parse(event.data);
  dataAcquisitionInfo = data.dataAcquisitionInfo;
  list = data.list;
  // console.log(111,dataAcquisitionInfo)
  arrSpecial = []
  // 处理数据
  handleData()
}

function handleData(){
  // 遍历数据列表
  list.forEach((item,index)=>{
    let num = 0;
    let str = ''
    item.forEach(m=>{
      if(m.v.ps&&(m.v.ps.value=='检验子项'||m.v.ps.value=='检验项'||m.v.ps.value=='检验项分类')){
        if(m.v.ps&&m.v.ps.value=='检验项分类'){
          if(num==0){
            num++
            str = m.v.v+','
          }
        }
        if(m.v.ps&&m.v.ps.value=='检验项'){
          if(num==1){
            str = str+m.v.v + ','
            num++
          } else {
            str = m.v.v+','
            num++
          }
        }
        if(m.v.ps&&m.v.ps.value=='检验子项'){
          str = str+m.v.v
        }
        // 上面在记录检验项+检验子项的名称，如果数采包含检验项+检验子项的数据，则执行
        if(dataAcquisitionInfo[str]){
          let num = 0;//该检验项对应检验值的个数，用用判断最多有多少个检验值，如果多次采集超过了该个数，则赋值给最后一个检验值
          list[index].forEach(n=>{
            if(n.v.ps&&n.v.ps.value&&typeof n.v.ps.value == 'string'&&n.v.ps.value.includes('检验值')){
              num++
            }
          })
          list[index].forEach((n,i)=>{
            if(n.v.ps&&n.v.ps.value&&typeof n.v.ps.value == 'string'&&n.v.ps.value.includes('检验值')){
              let arr = n.v.ps.value.split('值')
              if(Array.isArray(dataAcquisitionInfo[str].value)){
                // 如果数据源为数组，则执行
                for (let i = 0; i < dataAcquisitionInfo[str].value.length; i++) {
                  if(i+1==arr[1]){
                    // 赋值数采优化检验项列表
                    arrSpecial.push(n.i)
                    let num0 = 0
                    if(n.v.ct&&n.v.ct.fa&&typeof n.v.ct.fa == 'string'&&n.v.ct.fa.includes('.')){
                      // 保留模板配置的小数点位数
                      let str0 = n.v.ct.fa.split('.')[1]
                      num0 = str0.length
                      n.v.v = dataAcquisitionInfo[str].value[i]?Number(dataAcquisitionInfo[str].value[i]).toFixed(num0):dataAcquisitionInfo[str].value[i]
                    }else{
                      // 直接赋值
                      n.v.v = dataAcquisitionInfo[str].value[i]
                    }
                    // 传递给主线程
                    result = {
                      method:'changeInput',
                      value:{
                        list:list,
                        n:n
                      }
                    }
                    self.postMessage(JSON.stringify(result))
                  }
                }
              }else{
                // 如果数据源为字符或数字，则执行
                if(arr[1] ==dataAcquisitionInfo[str].frequency){
                  // 如果数采次数等于检验值序号，则赋值给当前检验值
                  arrSpecial.push(n.i)
                  setTimeout(()=>{
                    let num0 = 0
                    if(n.v.ct&&n.v.ct.fa&&typeof n.v.ct.fa == 'string'&&n.v.ct.fa.includes('.')){
                      // 保留模板配置的小数点位数
                      let str0 = n.v.ct.fa.split('.')[1]
                      num0 = str0.length
                      n.v.v = dataAcquisitionInfo[str].value?Number(dataAcquisitionInfo[str].value).toFixed(num0):dataAcquisitionInfo[str].value
                    }else{
                      // 直接赋值
                      n.v.v = dataAcquisitionInfo[str].value
                    }
                    // 传递给主线程
                    result = {
                      method:'changeInput',
                      value:{
                        list:list,
                        n:n
                      }
                    }
                    self.postMessage(JSON.stringify(result))
                  },2000)
                }else if(Number(dataAcquisitionInfo[str].frequency)>num){
                  // 如果数采次数大于检验值序号，则赋值给最后一个检验值
                  if(n.v.ps.value.includes(num)){
                    arrSpecial.push(n.i)
                    setTimeout(()=>{
                      let num0 = 0
                      if(n.v.ct&&n.v.ct.fa&&typeof n.v.ct.fa == 'string'&&n.v.ct.fa.includes('.')){
                        // 保留模板配置的小数点位数
                        let str0 = n.v.ct.fa.split('.')[1]
                        num0 = str0.length
                        n.v.v = dataAcquisitionInfo[str].value?Number(dataAcquisitionInfo[str].value).toFixed(num0):dataAcquisitionInfo[str].value
                      }else{
                        // 直接赋值
                        n.v.v = dataAcquisitionInfo[str].value
                      }
                      // 传递给主线程
                      result = {
                        method:'changeInput',
                        value:{
                          list:list,
                          n:n
                        }
                      }
                      self.postMessage(JSON.stringify(result))
                    },2000)
                  }
                }
              }
            }
          })
        }
      }
    })
  })
  // if(arrSpecial[arrSpecial.length-1]){
  //   //优化数采较多数据时-记录最后一个检验项的id，用于后续处理
  //   setTimeout(()=>{
  //     result = {
  //       getDataTypeId:arrSpecial[arrSpecial.length-1],
  //     }
  //     self.postMessage(JSON.stringify(result))
  //   },0)
  // }
}
