package net.m3u8;

import net.m3u8.download.M3u8DownloadFactory;
import net.m3u8.listener.DownloadListener;
import net.m3u8.utils.Constant;
import net.m3u8.utils.DateUtils;

/**
 * @author xiongshao
 * @date 2022-06-29 15:39:26
 */
public final class m3u8 {

    public String downlandVideo(String url, String fileName, String platForm) {
        M3u8DownloadFactory.M3u8Download m3u8Download = M3u8DownloadFactory.getUrlInfo(url);
        // 设置生成目录
        String filePath = "D:\\m3u8_video\\" + fixFile(platForm) + "\\" + fixFile(fileName);
        m3u8Download.setDir(filePath);
        // 设置视频名称
        m3u8Download.setFileName(fixFile(fileName) + DateUtils.dateTimeNow());
        // 设置线程数
        m3u8Download.setThreadCount(100);
        // 设置重试次数
        m3u8Download.setRetryCount(100);
        // 设置连接超时时间（单位：毫秒）
        m3u8Download.setTimeoutMillisecond(10000L);
        // 设置日志级别 可选值：NONE INFO DEBUG ERROR
        m3u8Download.setLogLevel(Constant.INFO);
        // 设置监听器间隔（单位：毫秒）
        m3u8Download.setInterval(500L);
        // 添加额外请求头
        //Map<String, Object> headersMap = new HashMap<>();
        //headersMap.put("Content-Type", "text/html;charset=utf-8");
        //m3u8Download.addRequestHeaderMap(headersMap);
        // 如果需要的话设置http代理
        //m3u8Download.setProxy("172.50.60.3", 8090);
        // 添加监听器
        m3u8Download.addListener(new DownloadListener() {
            @Override
            public void start() {
                System.out.println("开始下载!");
            }

            @Override
            public void process(String downloadUrl, int finished, int sum, float percent) {
                System.out.println("下载网址:" + downloadUrl + "\t已下载" + finished + "个\t一共" + sum + "个\t已完成" + percent + "%");
            }

            @Override
            public void speed(String speedPerSecond) {
                System.out.println("下载速度:" + speedPerSecond);
            }

            @Override
            public void end() {
                System.out.println("下载完毕!");
            }
        });
        // 开始下载
        m3u8Download.start();
        return filePath + "\\" + fileName;
    }

    public static String fixFile(String str) {
        String regex = "[\\/:*?|<>\"]";
        return str.replaceAll(regex, "-").replaceAll(" ", "");
    }

}
