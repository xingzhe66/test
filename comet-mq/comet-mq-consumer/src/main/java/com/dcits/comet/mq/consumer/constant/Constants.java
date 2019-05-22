package com.dcits.comet.mq.consumer.constant;

/**
 * 消费者消息状态
 *
 * @author guihj
 */
public class Constants {

        /**
         * 第一次接收到消息，等待消费
         */
        public static final int   STATUS_INIT = 1;
        /**
         * 没有对应的topic或者tag消费该消息
         */
        public static final int STATUS_UNCOMSUMER = 2;
        /**
           已经消费
         */
        public static final int STATUS_SUCCESS = 3;
        /**
         * 消费异常
         */
        public static final int STATUS_EXCEPTION = 4;

}
