package com.xhr.mca.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xhr.mca.common.Result;
import com.xhr.mca.common.ResultUtil;
import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.Product;
import com.xhr.mca.entity.constant.Status;
import com.xhr.mca.service.ProductService;

import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping("/product")
@Log4j
public class ProductController {

	private final ProductService productService;

	@Autowired
	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	/**
	 * 查询所有上架的商品
	 * 
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Result list(@RequestParam(value = "page") Integer page, @RequestParam(value = "rows") Integer rows) {
		try {
			PageHelper.startPage(page, rows);
			List<Product> products = productService.selectAll(new Product(Status.YES.ordinal()));
			PageInfo<Product> pageInfo = new PageInfo<Product>(products);
			return ResultUtil.success(pageInfo);
		} catch (WebAppException e) {
			log.error("ProductController list 发生异常:", e);
			return ResultUtil.fail(e.geteConstants());
		}
	}

	/**
	 * 查询商品的明细
	 * 
	 * @param sn
	 * @return
	 */
	@RequestMapping(value = "/detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Result detail(@RequestParam(value = "sn") String sn) {
		try {
			return ResultUtil.success(productService.selectProductDetail(sn));
		} catch (WebAppException e) {
			log.error("ProductController detail 发生异常:", e);
			return ResultUtil.fail(e.geteConstants());
		}
	}

}
