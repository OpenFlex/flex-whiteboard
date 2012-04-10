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

package mx.collections.tests.vectorList {
	import flash.events.EventDispatcher;
	
	import mx.collections.VectorList;
	
	import org.flexunit.asserts.assertEquals;

	public class ItemRetrievalTest {

		[Test]
		public function getItemAtShouldReturnValueFirstIndex():void {
			const position:int = 0;
			const value:int  = 11;
			var vector:Vector.<int> = new <int>[ value, 5, 7, 9, 13 ];
			var vectorList:VectorList = new VectorList( vector );
			
			assertEquals( value, vectorList.getItemAt( position ) );
		}

		[Test]
		public function getItemAtShouldReturnValueFromArbitraryIndex():void {
			const position:int = 3;
			const value:int  = 11;
			var vector:Vector.<int> = new <int>[ 5, 7, 9, value, 13 ];
			var vectorList:VectorList = new VectorList( vector );
			
			assertEquals( value, vectorList.getItemAt( position ) );
		}

		[Test]
		public function getItemAtShouldReturnValueLastIndex():void {
			const position:int = 3;
			const value:int  = 11;
			var vector:Vector.<int> = new <int>[ 5, 7, 9, value ];
			var vectorList:VectorList = new VectorList( vector );
			
			assertEquals( value, vectorList.getItemAt( position ) );
		}

		[Test(expects="RangeError")]
		public function shouldThrowRangeErrorForInvalidIndex():void {
			const position:int = 3;
			const value:int  = 11;
			var vector:Vector.<int> = new <int>[ value ];
			var vectorList:VectorList = new VectorList( vector );
			
			vectorList.getItemAt( position );
		}

		[Test]
		public function getItemIndexSimpleValueAtFirstPosition():void {
			const position:int = 0;
			const value:int  = 11;
			var vector:Vector.<int> = new <int>[ value, 5, 7, 9, 13 ];
			var vectorList:VectorList = new VectorList( vector );
			
			assertEquals( position, vectorList.getItemIndex( value ) );
		}

		[Test]
		public function getItemIndexSimpleValueAtArbitraryPosition():void {
			const position:int = 3;
			const value:int  = 11;
			var vector:Vector.<int> = new <int>[ 5, 7, 9, value, 13 ];
			var vectorList:VectorList = new VectorList( vector );
			
			assertEquals( position, vectorList.getItemIndex( value ) );
		}

		[Test]
		public function getItemIndexSimpleValueAtLastPosition():void {
			const position:int = 3;
			const value:int  = 11;
			var vector:Vector.<int> = new <int>[ 5, 7, 9, value ];
			var vectorList:VectorList = new VectorList( vector );
			
			assertEquals( position, vectorList.getItemIndex( value ) );
		}

		[Test]
		public function getItemIndexNotFound():void {
			const position:int = -1;
			const value:int  = 11;
			var vector:Vector.<int> = new <int>[ 5, 7, 9 ];
			var vectorList:VectorList = new VectorList( vector );
			
			assertEquals( position, vectorList.getItemIndex( value ) );
		}

		[Test]
		public function getItemIndexComplexValue():void {
			const position:int = 1;
			const value:EventDispatcher = new EventDispatcher();
			var vector:Vector.<EventDispatcher> = new <EventDispatcher>[ new EventDispatcher(), value, new EventDispatcher() ];
			var vectorList:VectorList = new VectorList( vector );
			
			assertEquals( position, vectorList.getItemIndex( value ) );
		}
		
		public function ItemRetrievalTest() {
		}
	}
}