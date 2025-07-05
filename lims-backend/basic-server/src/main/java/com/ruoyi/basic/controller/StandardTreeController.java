package com.ruoyi.basic.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ruoyi.basic.service.*;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.basic.dto.CopyStandardProductListDto;
import com.ruoyi.basic.dto.FactoryDto;
import com.ruoyi.basic.dto.InsSampleReceiveDto;
import com.ruoyi.basic.dto.ResetTreeDragDTO;
import com.ruoyi.basic.pojo.StandardProductList;
import com.ruoyi.basic.pojo.StandardProductListSupplierAsk;
import com.ruoyi.basic.pojo.StandardTree;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/standardTree")
@Api(tags = "标准库")
public class StandardTreeController {

    private StandardTreeService standardTreeService;

    private StandardMethodService standardMethodService;

    private StandardProductListService standardProductListService;

    private StandardProductListSupplierAskService standardProductListSupplierAskService;

    @ApiOperation(value = "获取标准树")
    @GetMapping("/selectStandardTreeList")
    public Result selectStandardTreeList() {
        return Result.success(standardTreeService.selectStandardTreeList());
    }

    @ApiOperation(value = "获取标准树(检验下单)")
    @GetMapping("/selectStandardTreeList2")
    public Result selectStandardTreeList2() {
        return Result.success(standardTreeService.selectStandardTreeList());
    }

    @ApiOperation(value = "添加标准树")
    @PostMapping("/addStandardTree")
    public Result addStandardTree(@RequestBody StandardTree standardTree) {
        return Result.success(standardTreeService.addStandardTree(standardTree));
    }

    @ApiOperation(value = "根据标准树进行标准查询")
    @GetMapping("/selectsStandardMethodByFLSSM")
    public Result selectsStandardMethodByFLSSM(String tree) {
        return Result.success(standardMethodService.selectsStandardMethodByFLSSM(tree));
    }

    @ApiOperation(value = "修改标准库中的内容")
    @PostMapping("/upStandardProductList")
    public Result upStandardProductList(@RequestBody Map<String, String> map) {
        String str = map.get("str");
        StandardProductList list = JSON.parseObject(str, StandardProductList.class);
        return Result.success(standardProductListService.upStandardProductList(list));
    }

    @ApiOperation(value = "修改标准库区间")
    @PostMapping("/updateSection")
    public Result updateSection(@RequestBody Map<String, String> map) {
        String str = map.get("str");
        StandardProductList list = JSON.parseObject(str, StandardProductList.class);
        return Result.success(standardProductListService.updateSection(list));
    }

    @ApiOperation(value = "删除标准树的层级")
    @DeleteMapping("/delStandardTree")
    public Result delStandardTree(String tree) {
        return Result.success(standardTreeService.delStandardTree(tree));
    }

    @ApiOperation(value = "通过标准树查询对应的检验项目")
    @PostMapping("/selectStandardProductList")
    public Result selectStandardProductList(@RequestBody InsSampleReceiveDto insSample) {
        return Result.success(standardProductListService.selectStandardProductList(insSample));
    }

    @ApiOperation(value = "通过检验标准查询检验项目")
    @GetMapping("/selectStandardProductListByMethodId")
    public Result selectStandardProductListByMethodId(Integer id, String tree) {
        return Result.success(standardProductListService.selectStandardProductListByMethodId(id, tree));
    }

    @ApiOperation(value = "批量查询检验项目")
    @GetMapping("/selectStandardProductByMethodId")
    public Result selectStandardProductByMethodId(Integer id, String tree, Integer page, String laboratory, String item, String items) {
        return Result.success(standardProductListService.selectStandardProductByMethodId(id, tree, page, laboratory, item, items));
    }

    @ApiOperation(value = "批量查询所有检验项目和检验子项枚举")
    @GetMapping("/selectStandardProductEnumByMethodId")
    public Result selectStandardProductEnumByMethodId(Integer id, String tree, String item) {
        return Result.success(standardProductListService.selectStandardProductEnumByMethodId(id, tree, item));
    }

    @ApiOperation(value = "获取标准树下标准方法枚举")
    @GetMapping("/selectStandardMethodEnum")
    public Result selectStandardMethodEnum() {
        return Result.success(standardMethodService.selectStandardMethodEnum());
    }

    @ApiOperation(value = "获取产品架构")
    @GetMapping("/getStandardTree2")
    public Result getStandardTree2() {
        return Result.success(standardTreeService.getStandardTree2());
    }

    @ApiOperation(value = "批量修改项目内容")
    @PostMapping("/upStandardProducts")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "standardProductList", dataTypeClass = StandardProductList.class),
            @ApiImplicitParam(name = "ids", dataTypeClass = Integer.class)
    })
    public Result upStandardProducts(@RequestBody Map<String, Object> product) {
        return Result.success(standardTreeService.upStandardProducts(product));
    }


    @ApiOperation("标准库拖拽")
    @PostMapping("/resetTreeDrag")
    public Result resetTreeDrag(@RequestBody ResetTreeDragDTO resetTreeDragDTO) {
        standardProductListService.resetTreeDrag(resetTreeDragDTO);
        return Result.success();
    }

    @ApiOperation("标准库拖拽全部")
    @PostMapping("/resetTreeDragBatch")
    public Result resetTreeDragBatch(@RequestBody Map<String, Object> params) {
        List<StandardProductList> standardProductLists = (List<StandardProductList>) params.get("params");
        standardProductListService.resetTreeDragBatch(standardProductLists);
        return Result.success();
    }

    @ApiOperation(value = "标准树排序")
    @PostMapping("/updateTreeSort")
    public Result updateTreeSort(@RequestBody List<FactoryDto> list) {
        return Result.success(standardTreeService.updateTreeSort(list));
    }

    /*************************************************** 厂家特殊要求值处理 ************************************************************/

    @ApiOperation(value = "根据产品id查询厂家要求值绑定")
    @PostMapping("/selectSupplierAsk")
    public Result selectSupplierAsk(@RequestBody StandardProductListSupplierAsk supplierAsk) throws Exception {
        return Result.success(standardProductListSupplierAskService.selectByProductId(supplierAsk));
    }

    @ApiOperation(value = "新增厂家要求值绑定")
    @PostMapping("/addProductSupplierAsk")
    public Result addProductSupplierAsk(@RequestBody Map<String, String> map) {
        String str = map.get("str");
        StandardProductListSupplierAsk supplierAsk = JSON.parseObject(str, StandardProductListSupplierAsk.class);
        return Result.success(standardProductListSupplierAskService.addProductSupplierAsk(supplierAsk));
    }

    @ApiOperation(value = "更新厂家要求值绑定")
    @PostMapping("/updateProductSupplierAsk")
    public Result updateProductSupplierAsk(@RequestBody Map<String, String> map) {
        String str = map.get("str");
        StandardProductListSupplierAsk supplierAsk = JSON.parseObject(str, StandardProductListSupplierAsk.class);
        standardProductListSupplierAskService.updateProductSupplierAsk(supplierAsk);
        return Result.success();
    }

    @ApiOperation(value = "删除厂家要求值绑定")
    @DeleteMapping("/deleteProductSupplierAsk")
    public Result deleteProductSupplierAsk(Integer supplierAskId) {
        standardProductListSupplierAskService.removeById(supplierAskId);
        return Result.success();
    }

    @ApiOperation(value = "修改标准树")
    @PostMapping("/updateStandardTree")
    public Result updateStandardTree(@RequestBody StandardTree standardTree) {
        return Result.success(standardTreeService.updateStandardTree(standardTree));
    }

    @ApiOperation(value = "检验项要求值复制对比")
    @PostMapping("/copyStandardProductList")
    public Result copyStandardProductList(@RequestBody CopyStandardProductListDto copyStandardProductListDto) {
        return Result.success(standardProductListService.copyStandardProductList(copyStandardProductListDto));
    }

    @ApiOperation(value = "检验项要求值单独对比")
    @PostMapping("/copyStandardProductOne")
    public Result copyStandardProductOne(@RequestBody CopyStandardProductListDto copyStandardProductListDto) {
        return Result.success(standardProductListService.copyStandardProductOne(copyStandardProductListDto));
    }

    @ApiOperation(value = "检验项批量修改要求值要求描述")
    @PostMapping("/updateStandardProductListBatch")
    public Result updateStandardProductListBatch(@RequestBody Map<String, Object> param) {
        List<StandardProductList> standardProductList = (List<StandardProductList>) param.get("standardProductList");
        return Result.success(standardProductListService.updateBatchById(standardProductList));
    }

    @ApiOperation(value = "检验项复制排序")
    @PostMapping("/copyStandardProductSort")
    public Result copyStandardProductSort(@RequestBody CopyStandardProductListDto copyStandardProductListDto) {
        return Result.success(standardProductListService.copyStandardProductSort(copyStandardProductListDto));
    }
}
