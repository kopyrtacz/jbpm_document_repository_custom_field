what's elfinder-2.x-servlet
====================

elfinder-2.x-servlet implements a java servlet for elfinder-2.x connector

elfinder is an Open-source file manager for web, written in JavaScript using jQuery and jQuery UI.
see also http://elfinder.org

<img src="http://img.blog.csdn.net/20130825231837531">

for elfinder-1.2 users, please go to https://github.com/Studio-42/elfinder-servlet.

importing elfinder-2.x-servlet
====================
this project is released as an artifact on the central repostory

use


    <dependency>
        <groupId>com.github.bluejoe2008</groupId>
        <artifactId>elfinder-servlet-2</artifactId>
        <version>1.1</version>
    </dependency>

to add dependency in your pom.xml

building elfinder-2.x-servlet
====================
the source files includes:

* src/main/webapp : a normal j2ee application includes elfinder, WEB-INF...
* src/main/java: source codes for elfinder-servlet
* src/main/resources: source codes for elfinder-servlet

To build this project with maven run:

    mvn install

to run this project within a jetty container use:

    mvn jetty:run

using elfinder-2.x-servlet in your web apps
====================
just use following codes to tell elfinder to connect with server-side servlet:

		<script type="text/javascript" charset="utf-8">
			$(document).ready(function() {
				$('#elfinder').elfinder({
					url : 'elfinder-servlet/connector',
				});
			});
		</script>

in your web.xml, following codes should be added to enable the servlet:

	<servlet>
		<servlet-name>elfinder</servlet-name>
		<servlet-class>org.springframework.web.servlet.DispatcherServlet
		</servlet-class>
	</servlet>

	<servlet-mapping>
		<servlet-name>elfinder</servlet-name>
		<url-pattern>/elfinder-servlet/*</url-pattern>
	</servlet-mapping>

yes! elfinder-2.x-servlet is developed upon SpringFramework (http://springframework.org)

an example elfinder-servlet.xml configuration is shown below:

	<!-- find appropriate  command executor for given command-->
	<bean id="commandExecutorFactory"
		class="cn.bluejoe.elfinder.controller.executor.DefaultCommandExecutorFactory">
		<property name="classNamePattern"
			value="cn.bluejoe.elfinder.controller.executors.%sCommandExecutor" />
		<property name="map">
			<map>
			<!-- 
				<entry key="tree">
					<bean class="cn.bluejoe.elfinder.controller.executors.TreeCommandExecutor" />
				</entry>
			-->
			</map>
		</property>
	</bean>

	<!-- FsService is often retrieved from HttpRequest -->
	<!-- while a static FsService is defined here -->
	<bean id="fsServiceFactory" class="cn.bluejoe.elfinder.impl.StaticFsServiceFactory">
		<property name="fsService">
			<bean class="cn.bluejoe.elfinder.impl.DefaultFsService">
				<property name="serviceConfig">
					<bean class="cn.bluejoe.elfinder.impl.DefaultFsServiceConfig">
						<property name="tmbWidth" value="80" />
					</bean>
				</property>
				<property name="volumeMap">
					<!-- two volumes are mounted here -->
					<map>
						<entry key="A">
							<bean class="cn.bluejoe.elfinder.localfs.LocalFsVolume">
								<property name="name" value="MyFiles" />
								<property name="rootDir" value="/tmp/a" />
							</bean>
						</entry>
						<entry key="B">
							<bean class="cn.bluejoe.elfinder.localfs.LocalFsVolume">
								<property name="name" value="Shared" />
								<property name="rootDir" value="/tmp/b" />
							</bean>
						</entry>
					</map>
				</property>
				<property name="securityChecker">
					<bean class="cn.bluejoe.elfinder.impl.FsSecurityCheckerChain">
						<property name="filterMappings">
							<list>
								<bean class="cn.bluejoe.elfinder.impl.FsSecurityCheckFilterMapping">
									<property name="pattern" value="A_.*" />
									<property name="checker">
										<bean class="cn.bluejoe.elfinder.impl.FsSecurityCheckForAll">
											<property name="readable" value="true" />
											<property name="writable" value="true" />
										</bean>
									</property>
								</bean>
								<bean class="cn.bluejoe.elfinder.impl.FsSecurityCheckFilterMapping">
									<property name="pattern" value="B_.*" />
									<property name="checker">
										<bean class="cn.bluejoe.elfinder.impl.FsSecurityCheckForAll">
											<property name="readable" value="true" />
											<property name="writable" value="false" />
										</bean>
									</property>
								</bean>
							</list>
						</property>
					</bean>
				</property>
			</bean>
		</property>
	</bean>
	

Making a release
================

For a developer to make a release they need to have setup an account and with Sonatype and have a PGP key
for signing the release more details can be found at: http://central.sonatype.org/pages/apache-maven.html

Then to make a release you first tag the version and push this to github:

    mvn release:clean release:prepare

and if everything goes ok you can then release the actual artifact based on the tag:

    mvn release:perform

This will stage the artifacy on the Sonatype servers, once there it will be checked and it it's ok you can
then release it: http://central.sonatype.org/pages/releasing-the-deployment.html
