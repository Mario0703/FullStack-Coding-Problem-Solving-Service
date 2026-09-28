import UserSignup from '@/components/authentication/UserSignup.vue'
import LandingPage from '@/components/home/LandingPage.vue'
import ChallengePage from '@/components/challenges/ChallengePage.vue'
import ConfirmSignUp from '@/components/authentication/ConfirmSignUp.vue'
import LoginPage from '@/components/authentication/LoginPage.vue'
import UserLoginConfirmed from '@/components/authentication/UserLoginConfirmed.vue'
import UserPage from '@/components/user/UserPage.vue'
import ForgotPassword from '@/components/user/ForgotPassword.vue'
import AdminDashBoard from '@/components/AdminPages/AdminDashBoard.vue'
import SolveChallange from '@/components/code-execution/SolveChallange.vue'
import AdminLogin from '@/components/AdminPages/AdminLogin/AdminLogin.vue'
import AdminLoginConfirmed from '@/components/AdminPages/AdminLogin/AdminLoginConfirmed.vue'
import ViewSubmission from "@/components/user/ViewSubmission.vue";

export const routes = [
  {
    path: '/Signup',
    name: 'signup',
    component: UserSignup,
  },
  {
    path: '/',
    name: 'home',
    component: LandingPage,
  },
  {
    path: '/challenges',
    name: 'challenges',
    component: ChallengePage,
  },
  {
    path: '/confirm-signup',
    name: 'confirmSignup',
    component: ConfirmSignUp,
  },
  {
    path: '/Login',
    name: 'login',
    component: LoginPage,
  },
  {
    path: '/forgot-password',
    name: 'forgotPassword',
    component: ForgotPassword,
  },
  {
    path: '/login-confirmed',
    name: 'loginConfirmed',
    component: UserLoginConfirmed,
    meta: { requiresAuth: true },
  },
  {
    path: '/User',
    name: 'user',
    component: UserPage,
    meta: { requiresAuth: true },
  },
  {
    path: '/admin/dashboard',
    name: 'adminDashboard',
    component: AdminDashBoard,
    meta: { requiresAdmin: true },
  },
  {
    path: '/admin/login-confirmed',
    name: 'adminLoginConfirmed',
    component: AdminLoginConfirmed,
    meta: { requiresAdmin: true },
  },
  {
    path: '/challenges/:id/solve',
    name: 'solveChallenge',
    component: SolveChallange,
    meta: { requiresAuth: true },
  },
  {
    path: '/submissions/:submissionId',
    name: 'submissionDetails',
    component: ViewSubmission,
    meta: { requiresAuth: true },
  },
  {
    path: '/admin/AdminLogin',
    alias: '/AdminLogin',
    name: 'admin login',
    component: AdminLogin,
  }
];
