package com.dcits.comet.batch.reader;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

public class BatchFileReader<T> {
    private String batchname;
    private String filename;
    private T targetType;
    private String[] calumes;


    public ItemReader getReader(){
        FlatFileItemReader<T> reader = new FlatFileItemReader<T>();//1使用FlatFileItemReader读取文件。
        reader.setResource(new ClassPathResource(filename));//2使用FlatFileItemReader的setResource方法设置csv文件的路径。
        reader.setLineMapper(new DefaultLineMapper<T>() {{//3在此处对cvs文件的数据和领域模型类做对应映射。
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(calumes);
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<T>() {{
                setTargetType((Class<? extends T>) targetType.getClass());
            }});
        }});

        return reader;
    }

    public void setBatchname(String batchname) {
        this.batchname = batchname;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setCalumes(String[] calumes) {
        this.calumes = calumes;
    }
}
