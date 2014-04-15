Greyton - A DSL to make jclouds Groovy
======================================

This a DSL that makes working with the cloud very _groovy_. THe primary use case is DevOps.
All of the heavy lifting is done by the wonderful Apache JClouds library

Origin of Name
--------------

I needed a name starting with *Gr*, so I named after a small town in the Western Cape, South Africa. Being a
South African name it needs to be pronounded with a much more guttaral *G* that what one might be used to for *Groovy*.
However, no one would complain too much if you have a softer *Gr*.

Code Examples
-------------

As the code is in early stages of development, nothing is currently posted to OSS JFrog, Bintray or JCenter.
Compile locally and install to your local M2 repository via ```./gradlew build install```

### Bootstrap
```groovy
@Grab( 'org.ysb33r.groovy:greyton:0.1-SNAPSHOT' )
import static org.ysb33r.groovy.dsl.greyton.Cloud.cloud
```

### Create an EC2 Farm
```groovy
cloud {
    farms {
        ec2('EC2Example') {
            credentials "YOUR_AWSAccessKeyId", "YOUR_AWSSecretKey"
            ssh on
        }
    }
}

assert cloud.farms.EC2Example != null
```

### Create a template on a farm
```groovy
cloud {
    templates {
        // Note the farm created earlier is now a keyword inside the templates block
        EC2Example('MyTemplate') {
            profile smallest
            profile os64Bit
            os centos
        }
    }
}

assert cloud.templates.MyTemplate !=null
```

### Create local filesystem storage
```groovy
cloud {
   stores {
        filesystem ('fs_example') {
            overrides baseDir : targetDir
        }
    }
}

assert cloud.stores.fs_example != null
```