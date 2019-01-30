package cn.t.tool.redistool.common;

import redis.clients.jedis.HostAndPort;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class RedisConfiguration {

    private final Set<HostAndPort> hosts = new HashSet<>();

    public RedisConfiguration(HostAndPort... hostAndPorts) {
        this.hosts.addAll(Arrays.asList(hostAndPorts));
    }
    public RedisConfiguration(Collection<HostAndPort> hostAndPorts) {
        this.hosts.addAll(hostAndPorts);
    }

    public Set<HostAndPort> getHosts() {
        return hosts;
    }
}
