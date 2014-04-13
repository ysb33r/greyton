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

package org.ysb33r.groovy.dsl.greyton

import spock.lang.*
import static org.ysb33r.groovy.dsl.greyton.Cloud.cloud

/**
 * Created by schalkc on 10/04/2014.
 */
class FilesystemSpec extends Specification {

    static final targetDir = new File('build/tmp/filesystem')

    void setup() {
        if(targetDir.exists()) {
            assert targetDir.deleteDir()
        }
        targetDir.mkdirs()
    }

    def "Must be able to create a filesystem blob store" () {
        given:

        cloud {
            stores {
                filesystem ('test') {
                    overrides baseDir : targetDir
                }
            }
        }

        expect:
            cloud.stores.test
    }

    def "Credentials will be ignored"() {
        given:

        cloud {
            stores {
                filesystem ('test') {
                    overrides baseDir : targetDir
                    credentials 'foot','bed'
                }
            }
        }

        expect:
        cloud.stores.test

    }

    def "Deleting a non-existing blobstore should not throw an exception"() {
        given:
            cloud {
                stores {
                    delete 'foobar'
                }
            }

        expect:
            true
    }
}
