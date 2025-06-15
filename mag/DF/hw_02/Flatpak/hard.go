package main

import (
	"fmt"
	"io/ioutil"
	"log"
	"os"
	"os/exec"
)

func main() {
	// Create hard.txt with content "hard"
	content := []byte("hard")
	err := ioutil.WriteFile("hard.txt", content, 0644)
	if err != nil {
		log.Fatal(err)
	}
	fmt.Println("Created hard.txt")

	// Call the main program
	cmd := exec.Command("./simpleOut")
	cmd.Stdout = os.Stdout
	cmd.Stderr = os.Stderr
	err = cmd.Run()
	if err != nil {
		log.Printf("Error running ./simpleOut: %v\n", err)
	} else {
		fmt.Println("Executed ./simpleOut")
	}
}
