package com.kay.controller.backend;

import com.kay.common.ServerResponse;
import com.kay.pojo.Product;
import com.kay.service.IFileService;
import com.kay.service.IProductService;
import com.kay.util.PropertiesUtil;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


/**
 * Created by kay on 2018/3/20.
 * 商品管理
 */
@Controller
@RequestMapping("/manage/product")
public class ProductManageController {

    @Autowired
    private IProductService iProductService;

    @Autowired
    private IFileService iFileService;

    /**
     * 分页list
     */
    @GetMapping("/list")
    public ServerResponse list(@RequestParam(value = "pageNum",defaultValue = "1") int pageNum,@RequestParam(value = "pageSize",defaultValue = "10") int pageSize) {
        return iProductService.getManageProductList(pageNum, pageSize);
    }

    /**
     * 条件查询-分页查询
     */
    @GetMapping("/search")
    public ServerResponse searchList(String productName,Integer productId,@RequestParam(value = "pageNum",defaultValue = "1") int pageNum,@RequestParam(value = "pageSize",defaultValue = "10") int pageSize) {
        return iProductService.getManageSearchList(productId, productName,pageNum,pageSize);
    }

    /**
     * 更新或添加产品
     */
    @GetMapping("/save")
    public ServerResponse saveProduct(Product product) {
        return iProductService.saveOrUpdateProduct(product);
    }

    /**
     * 修改产品状态
     */
    @GetMapping("s/et_sale_status")
    public ServerResponse setSaleStatus(Integer productId,Integer status) {
        return iProductService.setSaleStatus(productId,status);
    }


    /**
     * 商品详情
     * @param productId
     * @return
     */
    @GetMapping("/detail")
    public ServerResponse getProductDetail(Integer productId) {
        return iProductService.getManageProductDetail(productId);
    }

    /**
     * 文件上传
     * @param file
     * @param request
     * @return
     */
    @GetMapping("/upload")
    public ServerResponse uploadFile(@RequestParam(value = "upload_file", required = false) MultipartFile file,
                                     HttpServletRequest request) {
        String path = request.getSession().getServletContext().getRealPath("upload");
        String uploadFilePath = iFileService.upload(file, path);
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + uploadFilePath;
        Map fileMap = new HashMap();
        fileMap.put("uri", uploadFilePath);
        fileMap.put("url", url);
        return ServerResponse.createBySuccess(fileMap);
    }

    /**
     * 富文本上传-------富文本上传根据使用的插件，有固定的返回值设置，这里使用Simditor
     *         {
     *            "success": true/false,
     *                "msg": "error message", # optional
     *            "file_path": "[real file path]"
     *        }
     */
    @GetMapping("/richtext_img_upload")
    public Map richTextUpload(@RequestParam(value = "upload_file",required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        Map resultMap = new HashMap();
        String path = request.getSession().getServletContext().getRealPath("upload");
        String uploadFilePath = iFileService.upload(file, path);
        if (StringUtils.isBlank(uploadFilePath)) {
            resultMap.put("success", false);
            resultMap.put("msg", "上传失败");
            return resultMap;
        }
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + uploadFilePath;
        resultMap.put("success", true);
        resultMap.put("msg", "上传成功");
        resultMap.put("file_path", url);
        //插件约定
        response.addHeader("Access-Control-Allow-Headers","X-File-Name");
        return resultMap;
    }
}
