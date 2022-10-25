This project implements a PIP plugin running ScriptEngine to retrieve attribute for subject/resource. Currently only Nodejs script engine supported.

## Performance
With local single thread sequential test where JavaScript queries a RESTful server to fetch some dummy attributes(no delay on server), 5,000,000 requests finish at average 2ms latency. 
RAM usage is around 70MB(after GC), CPU usage is around 21%(Intel Quad Core i5@2.70Ghz).