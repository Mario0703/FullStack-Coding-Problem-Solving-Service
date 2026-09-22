

type UserData = {
  email: string
  UserName: string
}

let user : UserData = {
  email: '',
  UserName: ''
}

const response = await fetch('http://localhost:3000/api/auth/me', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  credentials: 'include',
})

if(response.ok) {
  user = await response.json()
}

export default user

