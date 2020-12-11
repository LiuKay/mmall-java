package com.kay.controller.backend;

import com.github.pagehelper.PageInfo;
import com.kay.config.AppConfigProperties;
import com.kay.domain.Product;
import com.kay.domain.ProductStatusEnum;
import com.kay.service.FileService;
import com.kay.service.ProductService;
import com.kay.vo.ProductDetailVo;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
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
@RequestMapping("/manage/products")
public class ProductManageController {

    @Autowired
    private ProductService productService;

    @Autowired
    private FileService fileService;

    @Autowired
    private AppConfigProperties properties;

    /**
     * 分页list
     */
    @GetMapping("/list")
    public PageInfo list(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return productService.getManageProductList(pageNum, pageSize);
    }

    /**
     * 条件查询-分页查询
     */
    @GetMapping("/search")
    public PageInfo searchList(String productName, Integer productId,
                               @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                               @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return productService.getManageSearchList(productId, productName, pageNum, pageSize);
    }

    /**
     * 更新或添加产品
     */
    @GetMapping("/save")
    public void saveProduct(Product product) {
        productService.saveOrUpdateProduct(product);
    }

    /**
     * 修改产品状态
     */
    @GetMapping("/set_sale_status")
    public void setSaleStatus(@RequestParam @NonNull Integer productId, String status) {
        ProductStatusEnum productStatusEnum = ProductStatusEnum.valueOf(status.toUpperCase());
        if (productStatusEnum == null) {
            throw new IllegalArgumentException("status is not expected.");
        }
        productService.setSaleStatus(productId, productStatusEnum);
    }


    /**
     * 商品详情
     *
     * @param productId
     * @return
     */
    @GetMapping("/detail")
    public ProductDetailVo getProductDetail(@RequestParam @NonNull Integer productId) {
        return productService.getManageProductDetail(productId);
    }

    @GetMapping("/upload/img/")
    public Map<String, String> uploadFile(MultipartFile file) {
        String uploadFilePath = fileService.uploadImg(file);
        AppConfigProperties.FTPConfigProperties ftp = properties.getFtp();
        String prefix = "ftp://" + ftp.getServer() + ":" + ftp.getPort();
        Map<String, String> fileMap = new HashMap<>();
        fileMap.put("uri", uploadFilePath);
        fileMap.put("url", prefix);
        return fileMap;
    }

    /**
     * 富文本上传-------富文本上传根据使用的插件，有固定的返回值设置，这里使用Simditor
     * {
     * "success": true/false,
     * "msg": "error message", # optional
     * "file_path": "[real file path]"
     * }
     */
    @GetMapping("/upload/richtext_img")
    public Map<String, Object> richTextUpload(@RequestParam(value = "upload_file", required = false) MultipartFile file,
                                              HttpServletResponse response) {
        Map resultMap = new HashMap();
        String uploadFilePath = fileService.uploadImg(file);
        if (StringUtils.isBlank(uploadFilePath)) {
            resultMap.put("success", false);
            resultMap.put("msg", "Failed to upload.");
            return resultMap;
        }
        AppConfigProperties.FTPConfigProperties ftp = properties.getFtp();
        String path = "ftp://" + ftp.getServer() + ":" + ftp.getPort() + uploadFilePath;
        resultMap.put("success", true);
        resultMap.put("msg", "上传成功");
        resultMap.put("file_path", path);
        //插件约定
        response.addHeader("Access-Control-Allow-Headers", "X-File-Name");
        return resultMap;
    }
}
