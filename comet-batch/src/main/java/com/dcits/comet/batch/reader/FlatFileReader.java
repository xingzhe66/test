package com.dcits.comet.batch.reader;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class FlatFileReader {

//    @Bean
//    @StepScope
//    public ItemReader<BlackListDO> reader(@Value("#{jobParameters[inputFileBlack]}") String inputFile) throws IOException {
//        logger.info("inputFile:"+new ClassPathResource(inputFile).getURL().getPath());
//        if(inputFile == null){
//            logger.error("The blacklist reader file is null");
//            return null;
//        }
//        FlatFileItemReader<BlackListDO> reader = new FlatFileItemReader<BlackListDO>();
//
//        reader.setResource(new ClassPathResource(inputFile));
//
//        reader.setLineMapper(lineMapper());
//
//        reader.setLinesToSkip(1);
//
//        reader.open(JobCompletionNotificationListener.jobExecution.getExecutionContext());
//
//        return reader;
//    }
//
//
//    /**
//     * 读取文本行映射POJO
//     * @return
//     */
//    @Bean
//    @StepScope
//    public LineMapper<BlackListDO> lineMapper() {
//        DefaultLineMapper<BlackListDO> lineMapper = new DefaultLineMapper<BlackListDO>();
//        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
//        lineTokenizer.setDelimiter(",");
//        lineTokenizer.setStrict(false);
//        lineTokenizer.setNames(new String[] { "type","value","fraudType"});
//
//        BeanWrapperFieldSetMapper<BlackListDO> fieldSetMapper = new BeanWrapperFieldSetMapper<BlackListDO>();
//        fieldSetMapper.setTargetType(BlackListDO.class);
//        lineMapper.setLineTokenizer(lineTokenizer);
//        lineMapper.setFieldSetMapper(new BlackListFieldSetMapper());
//        return lineMapper;
//    }
}
