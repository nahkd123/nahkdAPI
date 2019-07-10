function _(dom) {
	return {
		element: dom,
		query: {
			parentTagName(query) {
				// Get the parent based on query
				if (!dom) return document.body;
				var result = dom;
				var found = false;
				while (!found) {
					if (result.tagName.toLowerCase() == query.toLowerCase() || result.tagName == "BODY") {
						return result;
					}
					result = result.parentElement;
				}
			}
		}
	};
}
var __ = {
	randomString() {
		var length = 16;
		var out = ""
		for (var i = 0; i < length; i++) out += Math.round(Math.random() * 35).toString(36);
		return out;
	},
	emptyStringArray(array) {
		var isEmpty = true;
		array.forEach(e => {isEmpty = isEmpty && (e === "")});
		return isEmpty;
	}
}