/*
I made this library to making block based programming language easier
*/

var SimpleBlok = {
	FieldType: {
		Label: Symbol("Label"),
		Newline: Symbol("Newline"),
		Text: Symbol("Text"),
		TextList: Symbol("TextList"),
		Conditions: Symbol("Conditions"),
		ChildBlocks: Symbol("ChildBlocks")
	},
	eventHandlers: {},
	createBlock(id) {
		var fields = [];
		for (var i = 0; i < (arguments.length - 1) / 3; i++) {
			fields.push(SimpleBlok.createField(arguments[i * 3 + 1], arguments[i * 3 + 2], arguments[i * 3 + 3]));
		}
		return {type: "block", id: id, fields: fields};
	},
	createField(id, type, value) {
		return {type: "field", id: id, fieldType: type, value: value};
	},
	createScript() {
		return {type: "script", blocks: [],
		addBlock(block) {
			this.blocks.push(block);
		}};
	},
	toElement(object) {
		if (object.type === "field") {
			var element = document.createElement("block-field");
			element.id = object.id;
			switch (object.fieldType) {
				case SimpleBlok.FieldType.Newline: {
					element.style.display = "block";
					break;
				}
				case SimpleBlok.FieldType.Label: {
					element.classList.add("label");
					element.innerText = object.value;
					break;
				}
				case SimpleBlok.FieldType.Number:
				case SimpleBlok.FieldType.Text: {
					element.classList.add("editable");
					element.textContent = object.value;
					element.contentEditable = "true";
					break;
				}
				case SimpleBlok.FieldType.TextList: {
					element.classList.add("list");
					object.value.forEach((ef) => {
						element.innerHTML += `${ef}<br>`;
					});
					element.contentEditable = "true";
					break;
				}
				case SimpleBlok.FieldType.Conditions: {
					element.classList.add("conditionslist");
					object.value.forEach((ef) => {
						var listEntry = document.createElement("list-entry-condition");
						listEntry.innerHTML = ``
						+ `<inputbox contenteditable>${ef.input1}</inputbox><inputbox contenteditable>${ef.type}</inputbox><inputbox contenteditable>${ef.input2}</inputbox>`;
						var deleteButton = document.createElement("button");
						deleteButton.classList.add("red");
						deleteButton.classList.add("deletebutton-condition");
						deleteButton.innerHTML = "x";
						listEntry.appendChild(deleteButton);
						element.appendChild(listEntry);
					});
					element.innerHTML += `<button class="aqua addbutton-condition">Add</button>`;
					break;
				}
				case SimpleBlok.FieldType.ChildBlocks: {
					element.classList.add("childblocks");
					element.appendChild(SimpleBlok.toElement(object.value));
					break;
				}
			}
			return element;
		} else if (object.type === "block") {
			var element = document.createElement("block-drag");
			element.id = object.id;
			element.draggable = true;
			object.fields.forEach(e => {element.appendChild(SimpleBlok.toElement(e));});
			return element;
		} else if (object.type === "script") {
			var element = document.createElement("block-script");
			object.blocks.forEach(e => {
				element.appendChild(SimpleBlok.toElement(e));
			});
			var newElementDrag = document.createElement("block-dragnew");
			newElementDrag.innerText = "Drag block from leftbar to add...";
			element.appendChild(newElementDrag);
			return element;
		}
	},
	convertToScript(element) {
		var script = SimpleBlok.createScript();
		for (var i = 0; i < element.children.length; i++) {
			var blockElement = element.children[i];
			var block = SimpleBlok.createBlock(blockElement.id);
			for (var j = 0; j < blockElement.children.length; j++) {
				var fieldElement = blockElement.children[j];
				var type = null;
				switch (fieldElement.classList[0]) {
					case "label": type = SimpleBlok.FieldType.Label; break;
					case "editable": type = SimpleBlok.FieldType.Text; break;
					case "conditionslist": type = SimpleBlok.FieldType.Conditions; break;
					case "childblocks": type = SimpleBlok.FieldType.ChildBlocks; break;
					case "list": type = SimpleBlok.FieldType.TextList; break;
				}
				if (type === SimpleBlok.FieldType.Conditions) {
					var conditions = [];
					for (var k = 0; k < fieldElement.children.length - 1; k++) conditions.push({
						type: fieldElement.children[k].children[1].innerText,
						input1: fieldElement.children[k].children[0].innerText,
						input2: fieldElement.children[k].children[2].innerText
					});
					block.fields.push(SimpleBlok.createField(fieldElement.id, type, conditions));
				} else if (type === SimpleBlok.FieldType.ChildBlocks) {
					block.fields.push(SimpleBlok.createField(fieldElement.id, type, SimpleBlok.convertToScript(fieldElement.children[0])));
				} else if (type === SimpleBlok.FieldType.TextList) {
					block.fields.push(SimpleBlok.createField(fieldElement.id, type, fieldElement.innerText.split('\n')));
				} else {
					block.fields.push(SimpleBlok.createField(fieldElement.id, type, fieldElement.innerText));
				}
			}
			script.addBlock(block);
		}
		return script;
	},
	toBlockList(sections) {
		var list = document.createElement("block-picklist");
		Object.keys(sections).forEach(e => {
			list.innerHTML += `<h1>${e}</h1>`;
			var blockList = sections[e];
			Object.keys(blockList).forEach(b => {
				list.appendChild(SimpleBlok.toElement(blockList[b]));
			});
		});
		return list;
	}
}