#![no_main]
#![no_std]

use log::info;
use uefi::prelude::*;

#[entry]
fn main() -> Status {
    uefi::helpers::init().unwrap();
    let system_table = unsafe { uefi::SystemTable::unsafe_get() }.unwrap();
    let stdout = system_table.stdout();

    let text = "Lan Vukusic";
    let mut displayed_text = String::new();

    for c in text.chars() {
        stdout.clear().unwrap();
        displayed_text.push(c);
        stdout.output_string(&displayed_text).unwrap();
        system_table.boot_services().stall(500_000); // 500 milliseconds delay
    }

    // Keep the final text on screen for a bit longer
    system_table.boot_services().stall(5_000_000);

    Status::SUCCESS
}
