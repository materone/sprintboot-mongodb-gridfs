package com.cmhit.demo.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.bson.Document;
//import com.cmhit.demo.domain.repo.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cmhit.demo.domain.bean.Customer;

@RestController
@RequestMapping("/mongo")
public class MongoController {

	// @Autowired
	// private Customer kdCustomer;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private GridFsTemplate gridfsTemplate;

	/**
	 * 处理excel与数据库之间的差异数据
	 */
	@RequestMapping("/list")
	public List<Customer> dobegin() throws Exception {
		System.out.println("List objects");
		// List<Customer> list = CustomerRepository.findAll();
		List<Customer> list2 = mongoTemplate.findAll(Customer.class);// 也可以
		return list2;
	}

	@RequestMapping("/add")
	public void doAdd(@RequestParam("CarNumber") String CarNumber, @RequestParam("HomeAddress") String HomeAddress,
			@RequestParam("cntFee") int CntFee, @RequestParam("name") String name) throws Exception {
		Customer c = new Customer();
		if ("".equalsIgnoreCase(CarNumber.trim())) {
			CarNumber = "Default";
		}
		c.setCarNumber(CarNumber);
		c.setName(name);
		c.setCntFee(CntFee);
		c.setHomeAddress(HomeAddress);
		mongoTemplate.save(c);
		System.out.println("Saved a record! ok");
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@ResponseBody
	private String upload(@RequestParam("file") MultipartFile file) throws Exception {
		// upload test
		String fileName = null;
		long size = 0;
		String type = "";
		String ret = "";
		Document meta = new Document();
		try {
			// get file info
			if (file.isEmpty()) {
				System.out.println("File is empty");
				String retmsg = "{\"ret\":\"200\",\"msg\":\"file name is required\"}";
				return retmsg;
			}
			fileName = file.getOriginalFilename();
			String shortname = fileName.substring(fileName.lastIndexOf(File.separatorChar) + 1);
			System.out.println("Mulitpart name:  " + file.getName());
			size = file.getSize();
			type = "images/jpeg";
			System.out.printf("Upload File: %s == %s == size: %d \n", fileName, shortname, size);
			// write to mongodb
			meta.append("companyid", "CH001");
			gridfsTemplate.store(file.getInputStream(), shortname, type, meta);
			// return
			ret = "{\"ret\":\"200\",\"msg\":\"Upliad is ok\"}";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return ret;
	}

	@RequestMapping(value = "/download", method = RequestMethod.GET)
	@ResponseBody
	private void download(@RequestParam("fname") String fileName, HttpServletResponse res) throws Exception {
		// To view or download file
		res.setContentType("application/octet-stream");
		res.addHeader("content-type", "application/octet-stream");
		res.setHeader("Content-Disposition", "attachment;filename=" + fileName);

		byte[] buff = new byte[1024];

		BufferedInputStream bis = null;
		OutputStream os = null;
		// if you want more meta info , you can uncomment the block
//		Query q = new Query(Criteria.where("filename").is(fileName));
//		GridFSFile gfs = gridfsTemplate.findOne(q);
//		InputStream is = gridfsTemplate.getResource(gfs).getInputStream();
		InputStream is = gridfsTemplate.getResource(fileName).getInputStream();
		os = res.getOutputStream();
		bis = new BufferedInputStream(is);
		int l = bis.read(buff);
		while (l != -1) {
			os.write(buff, 0, l);
			os.flush();
			l = bis.read(buff);
		}
		bis.close();
		is.close();
		System.out.println("Out finised " + fileName);
	}

	@RequestMapping(value = "/view", method = RequestMethod.GET)
	@ResponseBody
	private void view(@RequestParam("fname") String fileName, HttpServletResponse res) throws Exception {
		// To view or download file
		if (fileName != null) {
			int pos = fileName.lastIndexOf('.');
			String fileType = fileName.substring(pos + 1);
			System.out.println("FileType:"+fileType);
			if ("jpg".equalsIgnoreCase(fileType) || "jpeg".equalsIgnoreCase(fileType)
					|| "png".equalsIgnoreCase(fileType) || "gif".equalsIgnoreCase(fileType)) {
				res.setContentType("images/" + fileType);
			} else {
				System.out.println("Download bin file!");
				res.setContentType("application/octet-stream");
				res.addHeader("content-type", "application/octet-stream");
				res.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			}
		}

		byte[] buff = new byte[1024];

		BufferedInputStream bis = null;
		OutputStream os = null;
		// if you want more meta info , you can uncomment the block
//		Query q = new Query(Criteria.where("filename").is(fileName));
//		GridFSFile gfs = gridfsTemplate.findOne(q);
//		InputStream is = gridfsTemplate.getResource(gfs).getInputStream();
		InputStream is = gridfsTemplate.getResource(fileName).getInputStream();
		os = res.getOutputStream();
		bis = new BufferedInputStream(is);
		int l = bis.read(buff);
		while (l != -1) {
			os.write(buff, 0, l);
			os.flush();
			l = bis.read(buff);
		}
		bis.close();
		is.close();
		System.out.println("Out finised " + fileName);
	}
}
