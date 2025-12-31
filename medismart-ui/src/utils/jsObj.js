/**
 * Check if two JSON objects are equal
 * @param json1
 * @param json2
 * @return {boolean} true if the two JSON objects are equal, false otherwise
 */
export function isJsonEqual(json1, json2) {
  return JSON.stringify(json1) === JSON.stringify(json2);
}