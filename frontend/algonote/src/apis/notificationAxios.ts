import { axiosAuthApi } from '@/utils/instance'

const getNotificationsApi = () => {
  return axiosAuthApi()
    .get('/api/notifications')
    .then((reponse) => reponse.data)
    .catch((error) => console.log('알림 목록 조회 실패: ', error))
}

const checkReadNotificationApi = (notificationId: number) => {
  return axiosAuthApi()
    .delete(`/api/notifications/${notificationId}/mark-as-read`)
    .then(() => console.log('읽음 처리 성공'))
    .catch((error) => console.log('읽음 처리 실패:', error))
}

export { getNotificationsApi, checkReadNotificationApi }
