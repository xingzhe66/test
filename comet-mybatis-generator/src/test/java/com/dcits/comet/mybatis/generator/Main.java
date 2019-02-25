package com.dcits.comet.mybatis.generator;

import com.dcits.comet.mybatis.generator.mapper.FreemarkerUtil;
import com.dcits.comet.mybatis.generator.mapper.SqlModule;
import com.dcits.comet.mybatis.generator.yunyun.entity.CifClientPo;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Main {

	public static void main(final String[] args) {
		final File f = new File(Main.class.getResource("/").getPath());
		final String path = f.getPath();
		final SqlModule sqlModule = new SqlModule(CifClientPo.class);
		// 表名
		sqlModule.setTableName("CifClientPo");
		final FreemarkerUtil freemarkerUtil = new FreemarkerUtil();
		final Map<String, Object> module = new HashMap<>();
		module.put("module", sqlModule);
		System.out.println(path);
		freemarkerUtil.fprint("mapper.ftl", module, path + "/resources/", "CifClientMapper.xml");
	}

}
