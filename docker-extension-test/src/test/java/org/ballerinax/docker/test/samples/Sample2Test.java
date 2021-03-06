/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.ballerinax.docker.test.samples;

import io.fabric8.docker.api.model.ImageInspect;
import org.ballerinax.docker.exceptions.DockerPluginException;
import org.ballerinax.docker.test.utils.DockerTestUtils;
import org.ballerinax.docker.utils.DockerGenUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

import static org.ballerinax.docker.DockerGenConstants.ARTIFACT_DIRECTORY;
import static org.ballerinax.docker.test.utils.DockerTestUtils.getDockerImage;


public class Sample2Test implements SampleTest {

    private final String sourceDirPath = SAMPLE_DIR + File.separator + "sample2";
    private final String targetPath = sourceDirPath + File.separator + ARTIFACT_DIRECTORY;
    private final String dockerImage = "docker.abc.com/helloworld:v1.0";

    @BeforeClass
    public void compileSample() throws IOException, InterruptedException {
        Assert.assertEquals(DockerTestUtils.compileBallerinaFile(sourceDirPath, "hello_world_docker.bal"), 0);
    }


    @Test
    public void validateDockerfile() {
        File dockerFile = new File(targetPath + File.separator + "Dockerfile");
        Assert.assertTrue(dockerFile.exists());
    }

    @Test
    public void validateDockerImage() {
        ImageInspect imageInspect = getDockerImage(dockerImage);
        Assert.assertEquals(1, imageInspect.getContainerConfig().getExposedPorts().size());
        Assert.assertEquals("9090/tcp", imageInspect.getContainerConfig().getExposedPorts().keySet().toArray()[0]);
    }

    @AfterClass
    public void cleanUp() throws DockerPluginException {
        DockerGenUtils.deleteDirectory(targetPath);
        DockerTestUtils.deleteDockerImage(dockerImage);
    }
}
