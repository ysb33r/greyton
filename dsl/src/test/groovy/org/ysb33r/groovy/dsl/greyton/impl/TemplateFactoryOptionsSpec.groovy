// ============================================================================
// (C) Copyright Schalk W. Cronje 2014
//
// This software is licensed under the Apache License 2.0
// See http://www.apache.org/licenses/LICENSE-2.0 for license details
//
// Unless required by applicable law or agreed to in writing, software distributed under the License is
// distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and limitations under the License.
//
// ============================================================================
package org.ysb33r.groovy.dsl.greyton.impl

import com.google.common.base.Function
import com.google.common.base.Predicate
import org.jclouds.compute.domain.ComputeType
import org.jclouds.compute.domain.Hardware
import org.jclouds.compute.domain.Image
import org.jclouds.compute.domain.OsFamily
import org.jclouds.compute.domain.Processor
import org.jclouds.compute.domain.Template
import org.jclouds.compute.domain.TemplateBuilder
import org.jclouds.compute.domain.TemplateBuilderSpec
import org.jclouds.compute.domain.Volume
import org.jclouds.compute.options.TemplateOptions
import org.jclouds.domain.Location
import org.jclouds.domain.ResourceMetadata
import spock.lang.Specification

/**
 * Created by schalkc on 15/04/2014.
 */
class TemplateFactoryOptionsSpec extends Specification {

    def dummyHardware = new Hardware() {
        @Override
        List<? extends Processor> getProcessors() {
            return null
        }

        @Override
        int getRam() {
            return 0
        }

        @Override
        List<? extends Volume> getVolumes() {
            return null
        }

        @Override
        Predicate<Image> supportsImage() {
            return null
        }

        @Override
        String getHypervisor() {
            return null
        }

        @Override
        ComputeType getType() {
            return null
        }

        @Override
        String getProviderId() {
            return null
        }

        @Override
        String getName() {
            return "DUMMY_HARDWARE"
        }

        @Override
        String getId() {
            return null
        }

        @Override
        Set<String> getTags() {
            return null
        }

        @Override
        Location getLocation() {
            return null
        }

        @Override
        URI getUri() {
            return null
        }

        @Override
        Map<String, String> getUserMetadata() {
            return null
        }

        @Override
        int compareTo(ResourceMetadata<ComputeType> o) {
            return 0
        }
    }
    def dummyTestBuilder = new TemplateBuilder() {
        def testOsFamily = null
        boolean smallestCalled = false
        boolean biggestCalled = false
        boolean fastestCalled = false
        boolean os64BitCalled = false
        Integer testMinCores = null
        String fromCalled = null
        Hardware fromHardware = null

        @Override
        TemplateBuilder any() {
            return this
        }

        @Override
        TemplateBuilder fromHardware(Hardware hardware) {
            fromHardware = hardware
            return this
        }

        @Override
        TemplateBuilder fromImage(Image image) {
            return this
        }

        @Override
        TemplateBuilder fromTemplate(Template image) {
            return this
        }

        @Override
        TemplateBuilder from(TemplateBuilderSpec spec) {
            return this
        }

        @Override
        TemplateBuilder from(String spec) {
            fromCalled = spec
            return this
        }

        @Override
        TemplateBuilder smallest() {
            smallestCalled = true
            return this
        }

        @Override
        TemplateBuilder fastest() {
            fastestCalled = true
            return this
        }

        @Override
        TemplateBuilder biggest() {
            biggestCalled = true
            return this
        }

        @Override
        TemplateBuilder hypervisorMatches(String hypervisorRegex) {
            return this
        }

        @Override
        TemplateBuilder osFamily(OsFamily os) {
            testOsFamily = os
            return this
        }

        @Override
        TemplateBuilder locationId(String locationId) {
            return this
        }

        @Override
        TemplateBuilder imageId(String imageId) {
            return this
        }

        @Override
        TemplateBuilder hardwareId(String hardwareId) {
            return this
        }

        @Override
        TemplateBuilder osNameMatches(String osNameRegex) {
            return this
        }

        @Override
        TemplateBuilder osDescriptionMatches(String osDescriptionRegex) {
            return this
        }

        @Override
        TemplateBuilder osVersionMatches(String osVersionRegex) {
            return this
        }

        @Override
        TemplateBuilder osArchMatches(String architecture) {
            return this
        }

        @Override
        TemplateBuilder os64Bit(boolean is64bit) {
            os64BitCalled = true
            return this
        }

        @Override
        TemplateBuilder imageNameMatches(String imageNameRegex) {
            return this
        }

        @Override
        TemplateBuilder imageVersionMatches(String imageVersionRegex) {
            return this
        }

        @Override
        TemplateBuilder imageDescriptionMatches(String imageDescriptionRegex) {
            return this
        }

        @Override
        TemplateBuilder imageMatches(Predicate<Image> condition) {
            return this
        }

        @Override
        TemplateBuilder imageChooser(Function<Iterable<? extends Image>, Image> imageChooser) {
            return this
        }

        @Override
        TemplateBuilder minCores(double minCores) {
            testMinCores = minCores
            return this
        }

        @Override
        TemplateBuilder minRam(int megabytes) {
            return this
        }

        @Override
        TemplateBuilder minDisk(double gigabytes) {
            return this
        }

        @Override
        Template build() {
            return this
        }

        @Override
        TemplateBuilder options(TemplateOptions options) {
            return this
        }
    }

    def "Must be able to construct from a TemplateBuilder"() {
        given:
            def test=new TemplateFactoryOptions(dummyTestBuilder)
            
        expect:
            test != null
    }

    def "Must be able to configure from closure"() {
        given:
        TemplateFactoryOptions.bind(dummyTestBuilder) {
            os centos
            profile smallest
            profile fastest, biggest, os64Bit
            minCores 8
        }


        expect:
            dummyTestBuilder.testOsFamily == OsFamily.CENTOS
            dummyTestBuilder.smallestCalled == true
            dummyTestBuilder.fastestCalled == true
            dummyTestBuilder.biggestCalled == true
            dummyTestBuilder.os64BitCalled == true
            dummyTestBuilder.testMinCores == 8
    }

    def "The 'from' keyword must be able to distinguish between a string a closure"() {
        given:
        TemplateFactoryOptions.bind(dummyTestBuilder) {
            from 'FooBar'

//            from {
//                hardware dummyHardware
//                //image dummyImage
//                //template dummyTemplate
//                //templateSpec dummyTemplateSpec
//            }
        }


        expect:
        dummyTestBuilder.fromCalled == 'FooBar'
        //dummyTestBuilder.fromHardware.name == 'DUMMY_HARDWARE'

    }
}
