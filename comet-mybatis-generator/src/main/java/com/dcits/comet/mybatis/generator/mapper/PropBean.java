package com.dcits.comet.mybatis.generator.mapper;

public class PropBean {
	/**
	 * 数据库列名
	 */
	private String dbname;
	/**
	 * po中的列类型
	 * JDBC Type           Java Type
	 * CHAR                String
	 * VARCHAR             String
	 * LONGVARCHAR         String
	 * NUMERIC             java.math.BigDecimal
	 * DECIMAL             java.math.BigDecimal
	 * BIT                 boolean
	 * BOOLEAN             boolean
	 * TINYINT             byte
	 * SMALLINT            short
	 * INTEGER             INTEGER
	 * BIGINT              long
	 * REAL                float
	 * FLOAT               double
	 * DOUBLE              double
	 * BINARY              byte[]
	 * VARBINARY           byte[]
	 * LONGVARBINARY       byte[]
	 * DATE                java.sql.Date
	 * TIME                java.sql.Time
	 * TIMESTAMP           java.sql.Timestamp
	 * CLOB                Clob
	 * BLOB                Blob
	 * ARRAY               Array
	 * DISTINCT            mapping of underlying type
	 * STRUCT              Struct
	 * REF                 Ref
	 * DATALINK            java.net.URL
	 */
	private String type;


	public String getDbname() {
		return dbname;
	}

	public void setDbname(String dbname) {
		this.dbname = dbname;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
