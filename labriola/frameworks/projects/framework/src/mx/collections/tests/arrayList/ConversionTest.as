////////////////////////////////////////////////////////////////////////////////
//
//  Licensed to the Apache Software Foundation (ASF) under one or more
//  contributor license agreements.  See the NOTICE file distributed with
//  this work for additional information regarding copyright ownership.
//  The ASF licenses this file to You under the Apache License, Version 2.0
//  (the "License"); you may not use this file except in compliance with
//  the License.  You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////

package mx.collections.tests.arrayList {
	
	import mx.collections.ArrayList;
	
	import org.flexunit.assertThat;
	import org.flexunit.asserts.assertEquals;
	import org.hamcrest.object.equalTo;
	
	public class ConversionTest {
		[Test]
		public function toStringMethodShouldBeArrayToStringWhenProvidedArray():void {
			var array:Array = [ 5, 4, 3, 1, 2, 5 ];
			var arrayList:ArrayList = new ArrayList( array );
			assertEquals( array.toString(), arrayList.toString() );
		}
		
		[Test]
		public function toStringShouldBeEmptyStringWithNullSource():void {
			var arrayList:ArrayList = new ArrayList( null );
			assertEquals( "", arrayList.toString() );
		}
		
		[Test]
		public function toArrayShouldReturnArrayWithSameElementsAsArray():void {
			var array:Array = [ 5, 4, "3", 1, "Bob", 5 ];
			var arrayList:ArrayList = new ArrayList( array );
			assertThat( arrayList.toArray(), equalTo( array ) );
		}
		
		public function ConversionTest() {
		}
	}
	
}