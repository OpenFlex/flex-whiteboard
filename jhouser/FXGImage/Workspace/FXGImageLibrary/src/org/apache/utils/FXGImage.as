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

package org.apache.utils
{
	import flash.display.DisplayObject;
	import flash.display.Sprite;
	
	import mx.core.UIComponent;
	
	public class FXGImage extends UIComponent
	{
		public function FXGImage(source:Class = null)
		{
			if(source){
				this.source = source;
			}
			super();
		}
		
		private var _source : Class;
		
		/**
		 * @private
		 * This property is used to tell if the source has changed (or not). 
		 */
		protected var sourceChanged :Boolean = true;
		
		/**
		 * This property will be used to reference the class we want to use for the display.  
		 * Only tested with FXG files; but in theory any class name should work, such as an embedded library symbol from a Flash Pro SWF.  
		 */
		public function get source():Class
		{
			return _source;
		}
		
		public function set source(value:Class):void
		{
			_source = value;
			sourceChanged = true;
			this.commitProperties();
		}
		

		/**
		 * This will contain the instance created from the source. 
		 */
		protected var imageInstance : DisplayObject;
		

		private var _xOffset :int = 0;

		/**
		 * If we want to off set the X position of the asset; you can use this.  
		 * 
		 * @default 0
		 */
		public function get xOffset():int
		{
			return _xOffset;
		}

		/**
		 * @private
		 */
		public function set xOffset(value:int):void
		{
			_xOffset = value;
			this.invalidateDisplayList();
		}


		private var _yOffset :int = 0;

		/**
		 * If we want to off set the Y position of the asset; you can use this.  
		 * 
		 * @default 0
		 */
		public function get yOffset():int
		{
			return _yOffset;
		}

		/**
		 * @private
		 */
		public function set yOffset(value:int):void
		{
			_yOffset = value;
			this.invalidateDisplayList();
		}

		
		private var _heightIncrement :int = 0;

		/**
		 * If we want to add a value to the asset's default height; you can use this property.  The default is zero, which means the asset 
		 * will be sized based on the height and width of this component.
		 * 
		 * @default 0
		 */
		public function get heightIncrement():int
		{
			return _heightIncrement;
		}

		/**
		 * @private
		 */
		public function set heightIncrement(value:int):void
		{
			_heightIncrement = value;
			this.invalidateDisplayList();
		}


		private var _widthIncrement :int = 0;

		/**
		 * If we want to add a value to the asset's default width; you can use this property.  The default is zero, which means the asset 
		 * will be sized based on the height and width of this component.  
		 * 
		 * @default 0
		 */
		public function get widthIncrement():int
		{
			return _widthIncrement;
		}

		/**
		 * @private
		 */
		public function set widthIncrement(value:int):void
		{
			_widthIncrement = value;
			this.invalidateDisplayList();
		}
		
		
		/**
		 * @private 
		 */
		override protected function createChildren():void{
			super.createChildren();
			
			// if the source has changed we want to create, or recreate, the image instance
			if(this.sourceChanged){
				// if the instance has a value, then delete it
				if(this.imageInstance){
					this.removeChild(this.imageInstance);
					this.imageInstance = null;
				}
				
				// if we have a source value; create the source
				if(this.source){
					this.imageInstance = new source();
					this.imageInstance.x = 0 + xOffset;
					this.imageInstance.y = 0 + yOffset;
					this.addChild(this.imageInstance);
				}
				this.sourceChanged = false;
				
			}
		}
		
		/**
		 * @private 
		 */
		override protected function commitProperties():void{
			super.commitProperties();

			if(this.sourceChanged){
				// if the source changed re-created it; which is done in createChildren();
				this.createChildren();
			}
		}
		
		/**
		 * @private 
		 */
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void{
			super.updateDisplayList(unscaledWidth, unscaledHeight);

			// size the element.
			// I don't remember why I Wrote the code to check for unscaledHeight and unscaledWidth being 0
			if(unscaledHeight != 0){
				this.imageInstance.height = unscaledHeight + this.heightIncrement;
			}
			if(unscaledWidth != 0){
				this.imageInstance.width = unscaledWidth + this.widthIncrement;
			}
			
			// position the element
			this.imageInstance.x = 0 + xOffset;
			this.imageInstance.y = 0 + yOffset;

			
		}
		
	}
}