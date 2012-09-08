# klogd2

A new implementation of [klogd](https://github.com/leandrosilva/klogd) but in Java.

## What is klog?

Klog is nothing but a simple program to stream [Syslog](http://www.syslog.org) messages to a [Kafka](http://incubator.apache.org/kafka) server.

## Why a new version?

I'd want to try [Syslog4j](http://syslog4j.org) on the server side, because I know it's a rock solid stuff and all those cool kids are using it, e.g. [Graylog2](http://graylog2.org).

## Getting Started

### 1) Make sure you have Kafka up and running properly

* [Kafka - Quick Start](http://incubator.apache.org/kafka/quickstart.html)

### 2) Build klogd2

    $ git clone git@github.com:leandrosilva/klogd2.git
    $ mvn compile assembly:single
    $ ls -la target/klogd2-0.0.1-SNAPSHOT-jar-with-dependencies.jar

### 3) Configure Syslog messages routing

On Mac OS X, for example, you have to edit /etc/syslog.conf to include:

    *.info;authpriv,remoteauth,ftp,install,internal.none  @127.0.0.1:1514

### 4) Re-launch Syslog daemon to reflex the new configuration

On Mac OS X, for example, you have to:

    $ launchctl unload /System/Library/LaunchDaemons/com.apple.syslogd.plist
    $ launchctl load /System/Library/LaunchDaemons/com.apple.syslogd.plist

### 5) Start klogd

    $ ZOOKEEPER=127.0.0.1:2181 java -jar target/klogd2-0.0.1-SNAPSHOT-jar-with-dependencies.jar 

If you don't provide ZOOKEEPER environment variable, klogd2 is going to assume **127.0.0.1:2181** as default.

### 6) Test it now!

At one terminal:

    $ KAFKA_HOME/bin/kafka-console-consumer.sh --zookeeper 127.0.0.1:2181 --topic klog2

And at another:

    $ logger -p local0.info -t test.app "blah blah blah info info info"

## Copyright

Copyright (c) 2012 Leandro Silva <leandrodoze@gmail.com>

Permission is hereby granted, free of charge, to any person obtaining a copy of this software
and associated documentation files (the "Software"), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute,
sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
