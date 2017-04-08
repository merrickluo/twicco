import React from 'react'
import { View } from 'react-native'
import { NavigationActions } from 'react-navigation'

export default class BaseScreen extends React.Component {
  navigate = (routeName, params, replace) => {
    if (replace) {
      const action = NavigationActions.reset({
        index: 0,
        actions: [
          NavigationActions.navigate({ routeName: routeName, params: params })
        ]
      })
      this.props.navigation.dispatch(action)
    } else {
      this.props.navigation.navigate(routeName, params)
    }
  }

  render() {
    return (
      <View />
    )
  }
}
