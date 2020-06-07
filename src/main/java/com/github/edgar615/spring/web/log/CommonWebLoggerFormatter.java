package com.github.edgar615.spring.web.log;

/**
 * https://httpd.apache.org/docs/trunk/logs.html#common
 *
 * <pre>
 * 127.0.0.1 - frank [10/Oct/2000:13:55:36 -0700] "GET /apache_pb.gif HTTP/1.0" 200 2326
 * </pre>
 *
 * <ul>
 * <li>127.0.0.1 (%h)：This is the IP address </li>
 * <li>frank (%u)：This is the userid of the person requesting the document as determined by HTTP
 * authenticatio </li>
 * <li>[10/Oct/2000:13:55:36 -0700] (%t)：The time that the request was received. </li>
 * <li>"GET /apache_pb.gif HTTP/1.0" (\"%r\")： The request line from the client is given in double
 * quotes.The request line contains a great deal of useful information. First, the method used by
 * the client is GET. Second, the client requested the resource /apache_pb.gif, and third, the
 * client used the protocol HTTP/1.0. It is also possible to log one or more parts of the request
 * line independently. For example, the format string "%m %U%q %H" will log the method, path,
 * query-string, and protocol, resulting in exactly the same output as "%r"</li>
 * <li>200 (%>s)：This is the status code that the server sends back to the client. T</li>
 * <li>2326 (%b)
 * The last part indicates the size of the object returned to the client, not including the response
 * headers. If no content was returned to the client, this value will be "-". To log "0" for no
 * content, use %B instead.</li>
 * </ul>
 *
 * Response
 *
 * <pre>
 * Outgoing Response: 2d66e4bc-9a0d-11e5-a84c-1f39510f0d6b
 * Duration: 25 ms
 * HTTP/1.1 200
 * Content-Type: application/json
 *
 * {"value":"Hello world!"}
 * </pre>
 */
public class CommonWebLoggerFormatter {

}
