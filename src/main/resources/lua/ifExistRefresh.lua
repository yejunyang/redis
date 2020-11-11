--
-- Created by IntelliJ IDEA.
-- User: 81526
-- Date: 2020/11/11
-- Time: 23:28
-- To change this template use File | Settings | File Templates.
--

local result = redis.call("get",KEYS[1])
if result ~= nil then
    redis.call("setex",KEYS[1],2,ARGV[1])
    return result
else
    return 0
end

