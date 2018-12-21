# UIAdaptive
UI适配、刘海屏适配
How to
To get a Git project into your build:

Step 1. Add the JitPack repository to your build file

gradle
Add it in your root build.gradle at the end of repositories:

allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
  Step 2. Add the dependency
  
  dependencies {
	        implementation 'com.github.freakcsh:UIAdaptive:V1.0'
	}
  
  maven
  Step 1. Add the JitPack repository to your build file
  <repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
    
    Step 2. Add the dependency
    
    <dependency>
	    <groupId>com.github.freakcsh</groupId>
	    <artifactId>UIAdaptive</artifactId>
	    <version>V1.0</version>
	</dependency>
        
    
  
  
  
