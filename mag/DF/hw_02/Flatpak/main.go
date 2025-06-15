package flatpak

import "os"

func main() {
	data := []byte("helo")
	err := os.WriteFile("out.txt", data, 0644)
	if err != nil {
		panic(err)
	}
}
