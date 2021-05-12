use serde_json::Value;

pub fn extract_urls(value: &Value) -> Vec<String> {
    let mut result = vec![];
    add_urls(value, &mut result);
    result
}

fn add_urls(value: &Value, result: &mut Vec<String>) {
    match value {
        Value::String(s) if s.starts_with("http") => result.push(s.to_string()),
        Value::Array(values) => {
            for value in values {
                add_urls(value, result);
            }
        }
        Value::Object(map) => {
            for value in map.values() {
                add_urls(value, result);
            }
        }
        _ => (),
    }
}
