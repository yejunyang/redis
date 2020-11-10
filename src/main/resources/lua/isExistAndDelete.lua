--
-- Created by IntelliJ IDEA.
-- User: 81526
-- Date: 2020/11/11
-- Time: 0:32
-- To change this template use File | Settings | File Templates.
--

if redis.call("get",KEYS[1]) == ARGV[1] then
    return redis.call("del",KEYS[1])
else
    return 0
end

