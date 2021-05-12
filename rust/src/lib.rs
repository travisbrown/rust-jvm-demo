mod count;
mod extract;

// Adapted from the asmble Rust examples.

use std::alloc::{self, Layout};
use std::ffi::CString;
use std::mem;
use std::os::raw::c_char;

#[no_mangle]
pub extern "C" fn count_scalar_values(ptr: *mut u8, len: usize) -> i32 {
    unsafe {
        let bytes = Vec::<u8>::from_raw_parts(ptr, len, len);
        let input = std::str::from_utf8(&bytes).unwrap();
        let json = serde_json::de::from_str(input).unwrap();
        count::count_scalar_values(&json)
    }
}

#[no_mangle]
pub extern "C" fn extract_urls(ptr: *mut u8, len: usize) -> *const c_char {
    unsafe {
        let bytes = Vec::<u8>::from_raw_parts(ptr, len, len);
        let input = std::str::from_utf8(&bytes).unwrap();
        let json = serde_json::de::from_str(input).unwrap();
        let urls = extract::extract_urls(&json);
        let cstr = CString::new(serde_json::to_string(&urls).unwrap()).unwrap();
        let result = cstr.as_ptr();
        mem::forget(cstr);
        result
    }
}

#[no_mangle]
pub extern "C" fn alloc(size: usize) -> *mut u8 {
    unsafe {
        let layout = Layout::from_size_align(size, std::mem::align_of::<u8>()).unwrap();
        alloc::alloc(layout)
    }
}

#[no_mangle]
pub extern "C" fn dealloc(ptr: *mut u8, size: usize) {
    unsafe {
        let layout = Layout::from_size_align(size, mem::align_of::<u8>()).unwrap();
        alloc::dealloc(ptr, layout);
    }
}
